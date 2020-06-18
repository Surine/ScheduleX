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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.MySeekBarChangeListener;
import cn.surine.schedulex.base.utils.Navigations;
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
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository;
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

import static android.app.Activity.RESULT_OK;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-30 15:23
 */
@SuppressLint("SetTextI18n")
public class ScheduleConfigFragment extends BaseBindingFragment<FragmentScheduleConfigBinding> {

    public static final int CHANGE_SCHEDULE_NAME = 100;
    public static final int CHANGE_WEEK_INFO = 101;
    public static final int CHANGE_BACKGROUND = 102;
    public static final int CHANGE_COURSE_ITEM_HEIGHT = 103;
    private static final int PICK_PHOTO = 1;

    private ScheduleViewModel scheduleViewModel;
    int scheduleId;
    private CourseViewModel courseViewModel;
    private Schedule schedule;
    private FragmentScheduleConfigBinding globalT;
    private int mSettingItemTag;

    public static final String SCHEDULE_ID = "SCHEDULE_ID";
    private TimeTableViewModel timeTableViewModel;

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

        Class[] classesForTimeTable = new Class[]{TimeTableRepository.class};
        Object[] argsForTimeTable = new Object[]{TimeTableRepository.abt.getInstance()};
        timeTableViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimeTable, argsForTimeTable)).get(TimeTableViewModel.class);

        scheduleId = getArguments() == null ? -1 : getArguments().getInt(SCHEDULE_ID);

        if (scheduleId == Prefs.getLong(Constants.CUR_SCHEDULE, -1L)) {
            t.deleteSchedule.setVisibility(View.GONE);
        }

        schedule = scheduleViewModel.getScheduleById(scheduleId);
        t.setData(schedule);


        /***
         * 捷径
         */
        if (getArguments() != null && (mSettingItemTag = getArguments().getInt(ScheduleListFragment.FUNCTION_TAG, -1)) != -1) {
            switch (mSettingItemTag) {
                case CHANGE_WEEK_INFO:
                    showTimeConfigDialog();
                    break;
                case CHANGE_COURSE_ITEM_HEIGHT:
                    showItemHeightDialog();
                    break;
                case CHANGE_BACKGROUND:
                    chooseBackgroundPicture();
                    break;
                case CHANGE_SCHEDULE_NAME:
                    modifyScheduleName();
                    break;
                default:
                    break;
            }
        }


        t.deleteSchedule.setOnClickListener(v -> CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg)
                , () -> {
                    //删除课表后需要删除所有相关课程
                    scheduleViewModel.deleteScheduleById(scheduleId);
                    courseViewModel.deleteCourseByScheduleId(scheduleId);
                    Toasts.toast(getString(R.string.schedule_is_delete));
                    NavHostFragment.findNavController(ScheduleConfigFragment.this).navigateUp();
                }, null).show());


        t.scheduleNameItem.setOnClickListener(v -> modifyScheduleName());
        t.scheduleWeekInfoItem.setOnClickListener(v -> showTimeConfigDialog());
        t.scheduleTimeTableItem.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(ScheduleConfigFragment.SCHEDULE_ID, scheduleId);
            Navigations.open(ScheduleConfigFragment.this, R.id.action_scheduleConfigFragment_to_timeTableListFragment, bundle);
        });
        try {
            t.textViewTimeTableSubtitle.setText(timeTableViewModel.getTimTableById(schedule.timeTableId).name);
        } catch (Exception e) {
            t.textViewTimeTableSubtitle.setText("No Data");
        }

        t.scheduleBackgroundItem.setOnClickListener(v -> chooseBackgroundPicture());
        t.scheduleBackgroundItem.setOnLongClickListener(v -> {
            schedule.imageUrl = "";
            scheduleViewModel.updateSchedule(schedule);
            Toasts.toast(App.context.getResources().getString(R.string.handle_success));
            return true;
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
            t.paletteColorSubTitle.setText(schedule.lightText ? R.string.white_txt : R.string.black_txt);
        });

        //设置是否展示周末开关
        t.showWeekSwitchs.setChecked(schedule.isShowWeekend);
        t.scheduleShowWeekItem.setOnClickListener(v -> {
            schedule.isShowWeekend = !schedule.isShowWeekend;
            scheduleViewModel.updateSchedule(schedule);
            t.showWeekSwitchs.setChecked(schedule.isShowWeekend);
            t.showWeekSubTitle.setText(schedule.isShowWeekend ? R.string.show_weekend : R.string.not_show_weekend);
        });

        //打开透明度配置窗口
        t.scheduleCourseAlphaItem.setOnClickListener(v -> showCourseItemAlphaDialog());

        //配置最大节次
        t.scheduleMaxSessionItem.setOnClickListener(v -> showMaxSessionDialog());
        //配置课表最大高度
        t.scheduleCourseItemHeightItem.setOnClickListener(v -> showItemHeightDialog());

        //是否显示时间表
        t.showTimesSwitch.setChecked(schedule.isShowTime);
        t.scheduleShowTimeItem.setOnClickListener(v -> {
            schedule.isShowTime = !schedule.isShowTime;
            scheduleViewModel.updateSchedule(schedule);
            t.showTimesSwitch.setChecked(schedule.isShowTime);
            t.scheduleShowTimeItemSubTitle.setText(schedule.isShowTime ? R.string.show_time : R.string.not_show_timetable);
        });

        t.changeCourseCardUi.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(SCHEDULE_ID,scheduleId);
            Navigations.open(ScheduleConfigFragment.this,R.id.action_scheduleConfigFragment_to_paletteFragment,bundle);
        });

        Bundle bundle = new Bundle();
        bundle.putInt(SCHEDULE_ID, scheduleId);
        t.export.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleConfigFragment_to_scheduleDataExport, bundle));

    }

    private void modifyScheduleName() {
        CommonDialogs.getEditDialog(activity(), schedule.name, true, s -> {
            schedule.name = s;
            globalT.courseNameSubTitle.setText(s);
            Toasts.toast(getString(R.string.update_success));
            scheduleViewModel.updateSchedule(schedule);
        }, null);
    }


    private void chooseBackgroundPicture() {
        RxPermissions rxPermissions = new RxPermissions(activity());
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                startChoosePicture();
            } else {
                Toasts.toast(getString(R.string.permission_is_denied));
            }
        });
    }


    /**
     * 课表item高度
     */
    private void showItemHeightDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        View view;
        bt.setDismissWithAnimation(true);
        bt.setContentView(view = Uis.inflate(activity(), R.layout.view_schedule_time));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);

        TextView tv = view.findViewById(R.id.dialog_title);
        SeekBar s1 = view.findViewById(R.id.seekBar);
        SeekBar s2 = view.findViewById(R.id.seekBar2);
        TextView t1 = view.findViewById(R.id.tvS1);
        TextView t2 = view.findViewById(R.id.tvS2);
        Button button = view.findViewById(R.id.button);

        tv.setText("配置课程格子高度");
        s2.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);


        s1.setMax(150);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            s1.setMin(30);
        }
        s1.setProgress(schedule.itemHeight);
        t1.setText(schedule.itemHeight + "dp");
        s1.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t1.setText(progress + "dp");
            }
        });
        button.setOnClickListener(v -> {
            if (s1.getProgress() < 30) {
                Toasts.toast(getString(R.string.cant_small_than_30));
            } else {
                schedule.itemHeight = s1.getProgress();
                globalT.scheduleCourseItemHeightSubtitle.setText(s1.getProgress() + "px");
                scheduleViewModel.updateSchedule(schedule);
                bt.dismiss();
            }
        });
    }


    /**
     * 最大节次
     */
    private void showMaxSessionDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        View view;
        bt.setDismissWithAnimation(true);
        bt.setContentView(view = Uis.inflate(activity(), R.layout.view_schedule_time));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);

        TextView tv = view.findViewById(R.id.dialog_title);
        SeekBar s1 = view.findViewById(R.id.seekBar);
        SeekBar s2 = view.findViewById(R.id.seekBar2);
        TextView t1 = view.findViewById(R.id.tvS1);
        TextView t2 = view.findViewById(R.id.tvS2);
        Button button = view.findViewById(R.id.button);

        tv.setText("配置最大节次");
        s2.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);


        s1.setMax(20);
        s1.setProgress(schedule.maxSession);
        t1.setText(schedule.maxSession + "节");
        s1.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t1.setText(progress + "节");
            }
        });
        button.setOnClickListener(v -> {
            if (s1.getProgress() == 0) {
                Toasts.toast(getString(R.string.param_is_illgal));
            } else {
                schedule.maxSession = s1.getProgress();
                globalT.scheduleMaxSessionSubtitle.setText(s1.getProgress() + "节");
                scheduleViewModel.updateSchedule(schedule);
                bt.dismiss();
            }
        });
    }


    /**
     * 课程卡片透明度
     */
    @SuppressLint("SetTextI18n")
    private void showCourseItemAlphaDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        View view;
        bt.setDismissWithAnimation(true);
        bt.setContentView(view = Uis.inflate(activity(), R.layout.view_schedule_time));
        bt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bt.show();
        view.animate().translationY(50);

        TextView tv = view.findViewById(R.id.dialog_title);
        SeekBar s1 = view.findViewById(R.id.seekBar);
        SeekBar s2 = view.findViewById(R.id.seekBar2);
        TextView t1 = view.findViewById(R.id.tvS1);
        TextView t2 = view.findViewById(R.id.tvS2);
        Button button = view.findViewById(R.id.button);

        tv.setText("配置透明度");
        s2.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);


        s1.setMax(10);
        s1.setProgress(schedule.alphaForCourseItem);
        t1.setText("L" + schedule.alphaForCourseItem);
        s1.setOnSeekBarChangeListener(new MySeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t1.setText("L" + progress);
            }
        });
        button.setOnClickListener(v -> {
            schedule.alphaForCourseItem = s1.getProgress();
            globalT.scheduleCourseAlphaSubTitle.setText("L" + s1.getProgress());
            scheduleViewModel.updateSchedule(schedule);
            bt.dismiss();
        });
    }


    /**
     * 时间配置
     */
    @SuppressLint("StringFormatMatches")
    private void showTimeConfigDialog() {
        BottomSheetDialog bt = new BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme);
        View view;
        bt.setDismissWithAnimation(true);
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
            schedule.imageUrl = Files.getFilePath(activity(), uri);
            scheduleViewModel.updateSchedule(schedule);
            Glide.with(activity()).load(new File(schedule.imageUrl)).into(globalT.backgroundPic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toasts.toast(getString(R.string.pic_choose_fail));
        }
    }

}
