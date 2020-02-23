package cn.surine.schedulex.super_import.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.databinding.FragmentLoginSuperBinding;
import cn.surine.schedulex.super_import.model.SuperCourse;
import cn.surine.schedulex.super_import.model.SuperResository;
import cn.surine.schedulex.super_import.viewmodel.SuperViewModel;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

public class SuperLoginFragment extends BaseBindingFragment<FragmentLoginSuperBinding> {
    private CourseViewModel courseViewModel;
    private ProgressDialog dialog;
    private long scheduleId;
    private ScheduleViewModel scheduleViewModel;
    private Dialog superListDialog;
    private SuperViewModel superViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_login_super;
    }

    @Override
    public void onInit(FragmentLoginSuperBinding fragmentLoginSuperBinding) {
        superViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{SuperResository.class}, new Object[]{SuperResository.abt.getInstance()})).get(SuperViewModel.class);
        fragmentLoginSuperBinding.setData(this.superViewModel);
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{CourseRepository.class}, new Object[]{CourseRepository.abt.getInstance()})).get(CourseViewModel.class);
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{ScheduleRepository.class}, new Object[]{ScheduleRepository.abt.getInstance()})).get(ScheduleViewModel.class);
        dialog = new ProgressDialog(activity());
        dialog.setCancelable(false);
        superViewModel.loginStatus.observe(getViewLifecycleOwner(), num -> {
            if (num == SuperViewModel.START_LOGIN) {
                dialog.setTitle(getString(R.string.warning));
                dialog.setMessage(getString(R.string.ready_to_login_super_class));
                dialog.show();
            } else if (num == SuperViewModel.LOGIN_SUCCESS) {
                dialog.dismiss();
                superListDialog = CommonDialogs.getSuperListDialog(activity(), Integer.parseInt(Dates.getDate("yyyy")) - 1, (i, i2) -> {
                    superViewModel.getCourseList(i, i2);
                    superListDialog.dismiss();
                });
                superListDialog.show();
            } else if (num == SuperViewModel.LOGIN_FAIL) {
                dialog.dismiss();
                Toasts.toast(getString(R.string.login_fail));
            }
        });
        superViewModel.getCourseStatus.observe(getViewLifecycleOwner(), num -> {
            if (num == SuperViewModel.START_FETCH) {
                dialog.setTitle(getString(R.string.warning));
                dialog.setMessage(getString(R.string.ready_to_fetch_course));
                dialog.show();
            } else if (num == SuperViewModel.FETCH_SUCCESS) {
                dialog.dismiss();
                if (getArguments() != null) {
                    Bundle arguments = getArguments();
                    String str = ScheduleInitFragment.SCHEDULE_NAME;
                    if (!TextUtils.isEmpty(arguments.getString(str))) {
                        scheduleId = scheduleViewModel.addSchedule(getArguments().getString(str), 24, 1);
                        Prefs.save(Constants.CUR_SCHEDULE, scheduleId);
                        List<Course> courseList = new ArrayList();
                        for (SuperCourse superCourse : superViewModel.getSuperCourseList().lessonList) {
                            Course course = new Course();
                            course.scheduleId = scheduleId;
                            StringBuilder sb = new StringBuilder();
                            sb.append(course.scheduleId);
                            sb.append("@");
                            sb.append(UUID.randomUUID());
                            sb.append(System.currentTimeMillis());
                            course.id = sb.toString();
                            course.coureName = superCourse.name;
                            course.teacherName = superCourse.teacher;
                            course.teachingBuildingName = superCourse.locale;
                            course.classDay = String.valueOf(superCourse.day);
                            course.color = Constants.COLOR_1[new Random().nextInt(Constants.COLOR_1.length)];
                            course.classSessions = String.valueOf(superCourse.sectionstart);
                            int i = (superCourse.sectionend - superCourse.sectionstart) + 1;
                            if (i > Constants.MAX_SESSION) {
                                i = Constants.MAX_SESSION;
                            }
                            course.continuingSession = String.valueOf(i);
                            StringBuilder sb2 = new StringBuilder();
                            for (int i2 = 0; i2 < Constants.MAX_WEEK; i2++) {
                                sb2.append(0);
                            }
                            for (String parseInt : superCourse.period.split(" ")) {
                                int parseInt2 = Integer.parseInt(parseInt);
                                sb2.replace(parseInt2 - 1, parseInt2, String.valueOf(1));
                            }
                            course.classWeek = sb2.toString();
                            courseList.add(course);
                        }
                        courseViewModel.saveCourseByDb(courseList, scheduleId);
                        Navigations.open(SuperLoginFragment.this, R.id.scheduleFragment);
                    }
                }else{
                    Toasts.toast(getString(R.string.arg_exception));
                }
            } else if (num == SuperViewModel.FETCH_FAIL) {
                dialog.dismiss();
                Toasts.toast(getString(R.string.fetch_fail_please_retry));
            }
        });
        fragmentLoginSuperBinding.superTip.setOnClickListener(view -> CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.super_tip), null, null).show());
    }


}
