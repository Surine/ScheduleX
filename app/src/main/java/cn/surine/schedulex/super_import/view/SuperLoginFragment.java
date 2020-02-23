package cn.surine.schedulex.super_import.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider.Factory;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
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
        this.superViewModel = (SuperViewModel) ViewModelProviders.of((Fragment) this, (Factory) InstanceFactory.getInstance(new Class[]{SuperResository.class}, new Object[]{SuperResository.abt.getInstance()})).get(SuperViewModel.class);
        fragmentLoginSuperBinding.setData(this.superViewModel);
        this.courseViewModel = (CourseViewModel) ViewModelProviders.of((Fragment) this, (Factory) InstanceFactory.getInstance(new Class[]{CourseRepository.class}, new Object[]{CourseRepository.abt.getInstance()})).get(CourseViewModel.class);
        this.scheduleViewModel = (ScheduleViewModel) ViewModelProviders.of((Fragment) this, (Factory) InstanceFactory.getInstance(new Class[]{ScheduleRepository.class}, new Object[]{ScheduleRepository.abt.getInstance()})).get(ScheduleViewModel.class);
        this.dialog = new ProgressDialog(activity());
        this.dialog.setCancelable(false);
        this.superViewModel.loginStatus.observe(getViewLifecycleOwner(), (Observer) obj -> SuperLoginFragment.this.lambda$onInit$1$SuperLoginFragment((Integer) obj));
        this.superViewModel.getCourseStatus.observe(getViewLifecycleOwner(), (Observer) obj -> SuperLoginFragment.this.lambda$onInit$2$SuperLoginFragment((Integer) obj));
        fragmentLoginSuperBinding.superTip.setOnClickListener(view -> SuperLoginFragment.this.lambda$onInit$3$SuperLoginFragment(view));
    }

    public /* synthetic */ void lambda$onInit$1$SuperLoginFragment(Integer num) {
        if (num.intValue() == 0) {
            this.dialog.setTitle(getString(R.string.warning));
            this.dialog.setMessage(getString(R.string.ready_to_login_super_class));
            this.dialog.show();
        }
        if (num.intValue() == 1) {
            this.dialog.dismiss();
            this.superListDialog = CommonDialogs.getSuperListDialog(activity(), Integer.parseInt(Dates.getDate("yyyy")) - 1, (i, i2) -> SuperLoginFragment.this.lambda$null$0$SuperLoginFragment(i, i2));
            this.superListDialog.show();
        }
        if (num.intValue() == 2) {
            this.dialog.dismiss();
            Toasts.toast(getString(R.string.login_fail));
        }
    }

    public /* synthetic */ void lambda$null$0$SuperLoginFragment(int i, int i2) {
        this.superViewModel.getCourseList(i, i2);
        this.superListDialog.dismiss();
    }

    public /* synthetic */ void lambda$onInit$2$SuperLoginFragment(Integer num) {
        if (num.intValue() == 0) {
            this.dialog.setTitle(getString(R.string.warning));
            this.dialog.setMessage(getString(R.string.ready_to_fetch_course));
            this.dialog.show();
        }
        if (num.intValue() == 1) {
            this.dialog.dismiss();
            if (getArguments() != null) {
                Bundle arguments = getArguments();
                String str = ScheduleInitFragment.SCHEDULE_NAME;
                if (!TextUtils.isEmpty(arguments.getString(str))) {
                    this.scheduleId = this.scheduleViewModel.addSchedule(getArguments().getString(str), 24, 1);
                    Prefs.save(Constants.CUR_SCHEDULE, Long.valueOf(this.scheduleId));
                    ArrayList arrayList = new ArrayList();
                    for (SuperCourse superCourse : this.superViewModel.getSuperCourseList().lessonList) {
                        Course course = new Course();
                        course.scheduleId = this.scheduleId;
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
                        arrayList.add(course);
                    }
                    this.courseViewModel.saveCourseByDb(arrayList, this.scheduleId);
                    Navigations.open(this, R.id.scheduleFragment);
                }
            }
            Toasts.toast(getString(R.string.arg_exception));
            return;
        }
        if (num.intValue() == 2) {
            this.dialog.dismiss();
            Toasts.toast(getString(R.string.fetch_fail_please_retry));
        }
    }

    public /* synthetic */ void lambda$onInit$3$SuperLoginFragment(View view) {
        CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.super_tip), null, null).show();
    }
}
