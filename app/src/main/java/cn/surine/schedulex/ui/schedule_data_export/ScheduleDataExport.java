package cn.surine.schedulex.ui.schedule_data_export;

import android.Manifest;
import android.annotation.SuppressLint;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.CalendarProviders;
import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Jsons;
import cn.surine.schedulex.base.utils.Objs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentDateExportBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-02-05 15:14
 */
public class ScheduleDataExport extends BaseBindingFragment<FragmentDateExportBinding> {
    private Schedule schedule;
    private CourseViewModel courseViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_date_export;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onInit(FragmentDateExportBinding t) {
        if(Objs.isNull(getArguments())){
            return;
        }

        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);


        int scheduleId = getArguments().getInt(ScheduleConfigFragment.SCHEDULE_ID);
        schedule = scheduleViewModel.getScheduleById(scheduleId);
        t.scheduleTitle.setText(schedule.name);

        t.exportIcs.setOnClickListener(v -> {
            RxPermissions permission = new RxPermissions(activity());
            permission.request(Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR).subscribe(aBoolean -> {
                if (aBoolean) {
                    exportIcs();
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied));
                }
            });
        });


        //导出为Json
        t.exportJson.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(activity());
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    saveToJson(t,scheduleId);
                } else {
                    Toasts.toast(getString(R.string.permission_is_denied));
                }
            });
        });

        t.exportExcel.setOnClickListener(v -> Toasts.toast("暂不支持！"));
        t.myQrCode.setOnClickListener(v -> Toasts.toast("暂不支持！"));
        t.other.setOnClickListener(v -> Toasts.toast("欢迎加群提出意见！"));
    }


    /**
     * 导出到日历
     * */
    private void exportIcs() {
        CalendarProviders.addEvent(activity(),"测试","测试文本",System.currentTimeMillis(),System.currentTimeMillis() + 60 * 1000);
    }

    /**
     * 保存成json
     * @param scheduleId
     * @param t
     * */
    private void saveToJson(FragmentDateExportBinding t, int scheduleId) {
        String fileName = schedule.name + "_" + scheduleId;
        if(Files.saveAsJson(fileName, Jsons.entityToJson(courseViewModel.getCourseByScheduleId(scheduleId)))){
            Snackbar.make(t.exportJson,"保存成功,路径 /Download/"+fileName+".json",Snackbar.LENGTH_SHORT).show();
        }else{
            Toasts.toast("保存失败！请稍后再试！");
        }
    }
}
