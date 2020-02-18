package cn.surine.schedulex.ui.schedule_config;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.MySeekBarChangeListener;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleConfigBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

import static android.app.Activity.RESULT_OK;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-30 15:23
 */
public class ScheduleConfigFragment extends BaseBindingFragment<FragmentScheduleConfigBinding> {


    private static final int PICK_PHOTO = 1;
    private ScheduleViewModel scheduleViewModel;
    int scheduleId;
    private CourseViewModel courseViewModel;
    private Schedule schedule;
    private FragmentScheduleConfigBinding globalT;

    public static final String SCHEDULE_ID = "SCHEDULE_ID";

    @Override
    public int layoutId() {
        return R.layout.fragment_schedule_config;
    }


    @SuppressLint("CheckResult")
    @Override
    protected void onInit(FragmentScheduleConfigBinding t) {
        globalT = t;
        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        scheduleId = getArguments() == null ? -1 : getArguments().getInt(ScheduleListFragment.SCHEDULE_ID);

        if (scheduleId == Prefs.getLong(Constants.CUR_SCHEDULE, -1L)) {
            t.deleteSchedule.setVisibility(View.GONE);
        }

        schedule = scheduleViewModel.getScheduleById(scheduleId);
        t.setData(schedule);

        t.deleteSchedule.setOnClickListener(v -> CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg)
                , () -> {
                    //删除课表后需要删除所有相关课程
                    scheduleViewModel.deleteScheduleById(scheduleId);
                    courseViewModel.deleteCourseByScheduleId(scheduleId);
                    Toasts.toast(getString(R.string.schedule_is_delete));
                    NavHostFragment.findNavController(ScheduleConfigFragment.this).navigateUp();
                }, null).show());


        t.scheduleNameItem.setOnClickListener(v -> BtmDialogs.showEditBtmDialog(activity(), schedule.name, true, s -> {
            schedule.name = s;
            t.courseNameSubTitle.setText(s);
            Toasts.toast(getString(R.string.update_success));
            scheduleViewModel.updateSchedule(schedule);
        }));


        t.scheduleWeekInfoItem.setOnClickListener(v -> showTimeConfigDialog());

        t.scheduleBackgroundItem.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(activity());
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    startChoosePicture();
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied));
                }
            });
        });
        if (!TextUtils.isEmpty(schedule.imageUrl)) {
            Glide.with(activity()).load(new File(schedule.imageUrl)).into(t.backgroundPic);
        }

        //设置颜色适配开关
        t.switchs.setChecked(schedule.lightText);
        t.schedulePaletteItem.setOnClickListener(v -> {
            schedule.lightText = !schedule.lightText;
            scheduleViewModel.updateSchedule(schedule);
            t.switchs.setChecked(schedule.lightText);
            t.paletteColorSubTitle.setText(schedule.lightText ?R.string.white_txt:R.string.black_txt);
        });

        //设置是否展示周末开关
        t.showWeekSwitchs.setChecked(schedule.isShowWeekend);
        t.scheduleShowWeekItem.setOnClickListener(v -> {
            schedule.isShowWeekend = !schedule.isShowWeekend;
            scheduleViewModel.updateSchedule(schedule);
            t.showWeekSwitchs.setChecked(schedule.isShowWeekend);
            t.showWeekSubTitle.setText(schedule.isShowWeekend ? R.string.show_weekend : R.string.not_show_weekend);
        });



        Bundle bundle = new Bundle();
        bundle.putInt(SCHEDULE_ID, scheduleId);
        t.export.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleConfigFragment_to_scheduleDataExport, bundle));

    }


    /**
     * 时间配置
     */
    @SuppressLint("StringFormatMatches")
    private void showTimeConfigDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity());
        View view;
        bt.setContentView(view = Uis.inflate(activity(), R.layout.view_schedule_time));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);

        SeekBar s1 = view.findViewById(R.id.seekBar);
        SeekBar s2 = view.findViewById(R.id.seekBar2);
        TextView t1 = view.findViewById(R.id.tvS1);
        TextView t2 = view.findViewById(R.id.tvS2);
        Button button = view.findViewById(R.id.button);

        final int[] mTotalWeek = new int[1];
        final int[] mCurWeek = new int[1];


        //初始化
        s1.setMax(30);
        s1.setProgress(schedule.totalWeek);
        s2.setMax(schedule.totalWeek);
        s2.setProgress(schedule.curWeek());
        mTotalWeek[0] = schedule.totalWeek;
        mCurWeek[0] = schedule.curWeek();
        t1.setText(getString(R.string.total_week, schedule.totalWeek));
        t2.setText(getString(R.string.current_week, schedule.curWeek()));

        //监听
        s1.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                s2.setMax(progress);
                t1.setText(getString(R.string.total_week, String.valueOf(progress)));
                mTotalWeek[0] = progress;
            }
        });

        s2.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t2.setText(getString(R.string.current_week, String.valueOf(progress)));
                mCurWeek[0] = progress;
            }
        });

        button.setOnClickListener(v -> {
            if (mTotalWeek[0] == 0 || mCurWeek[0] == 0) {
                Toasts.toast(getString(R.string.param_is_illgal));
            } else {
                schedule.totalWeek = mTotalWeek[0];
                schedule.termStartDate = Dates.getTermStartDate(mCurWeek[0]);
                globalT.scheduleWeekSubtitle.setText(t1.getText().toString() + t2.getText().toString());
                scheduleViewModel.updateSchedule(schedule);
                bt.dismiss();
            }
        });
    }


    /**
     * 开始选择图片
     */
    private void startChoosePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                handleImage(data);
            }
        }
    }


    /**
     * 处理图片
     *
     * @param data
     */
    @SuppressLint("CheckResult")
    private void handleImage(Intent data) {
        Uri uri = data.getData();
        ContentResolver cr = activity().getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            Toasts.toast(getString(R.string.update_success));
            schedule.imageUrl = Files.getFilePath(activity(),uri);
            scheduleViewModel.updateSchedule(schedule);
            Glide.with(activity()).load(new File(schedule.imageUrl)).into(globalT.backgroundPic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toasts.toast(getString(R.string.pic_choose_fail));
        }
    }

}
