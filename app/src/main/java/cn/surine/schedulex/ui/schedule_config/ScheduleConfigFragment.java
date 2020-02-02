package cn.surine.schedulex.ui.schedule_config;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.FileNotFoundException;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleConfigBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_list.ScheduleListFragment;

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

        t.deleteSchedule.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity());
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.delete_schedule_dialog_msg);
            builder.setNegativeButton(R.string.btn_ok, (dialog, which) -> {
                //删除课表后需要删除所有相关课程
                scheduleViewModel.deleteScheduleById(scheduleId);
                courseViewModel.deleteCourseByScheduleId(scheduleId);
                Toasts.toast(getString(R.string.schedule_is_delete));
                NavHostFragment.findNavController(ScheduleConfigFragment.this).navigateUp();
            });
            builder.setPositiveButton(R.string.btn_cancel, null);
            builder.show();
        });


        t.scheduleNameItem.setOnClickListener(v -> {

        });


        t.scheduleWeekInfoItem.setOnClickListener(v -> Toasts.toast("测试"));

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
            Uri uri = Uri.parse(schedule.imageUrl);
            Glide.with(activity()).load(uri).into(t.backgroundPic);
        }

        t.schedulePaletteItem.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleConfigFragment_to_themeListFragment));


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
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    handleImage(data);
                }
                break;
            default:
                break;
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
            globalT.backgroundPic.setImageBitmap(bitmap);
            Toasts.toast(getString(R.string.update_success));
            schedule.imageUrl = uri.toString();
            scheduleViewModel.updateSchedule(schedule);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toasts.toast(getString(R.string.pic_choose_fail));
        }
    }

}
