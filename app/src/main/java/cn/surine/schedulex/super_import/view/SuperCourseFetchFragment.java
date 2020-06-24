package cn.surine.schedulex.super_import.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import cn.surine.schedulex.BR;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentSuperCourseTermListBinding;
import cn.surine.schedulex.super_import.model.SuperCourse;
import cn.surine.schedulex.super_import.model.Term;
import cn.surine.schedulex.super_import.viewmodel.SuperRepository;
import cn.surine.schedulex.super_import.viewmodel.SuperViewModel;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;
import kotlin.Unit;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/4/9 19:56
 */
public class SuperCourseFetchFragment extends BaseBindingFragment<FragmentSuperCourseTermListBinding> {

    private BaseAdapter<Term> adapter;
    private LinearLayoutManager layoutManager;
    private SuperViewModel superViewModel;
    private ProgressDialog dialog;
    private long scheduleId;
    private ScheduleViewModel scheduleViewModel;
    private CourseViewModel courseViewModel;

    @Override
    protected void onInit(FragmentSuperCourseTermListBinding t) {
        superViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{SuperRepository.class}, new Object[]{SuperRepository.abt.getInstance()})).get(SuperViewModel.class);
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{ScheduleRepository.class}, new Object[]{ScheduleRepository.abt.getInstance()})).get(ScheduleViewModel.class);
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{CourseRepository.class}, new Object[]{CourseRepository.abt.getInstance()})).get(CourseViewModel.class);
        dialog = new ProgressDialog(activity());
        dialog.setCancelable(false);
        try {
            List<Term> data = (List<Term>) getArguments().getSerializable(SuperLoginFragment.TERM_DATA);
            //创建列表
            adapter = new BaseAdapter<>(data, R.layout.item_super_term, BR.term);
            t.recyclerview.setLayoutManager(layoutManager = new LinearLayoutManager(getActivity()));
            t.recyclerview.setAdapter(adapter);

            adapter.setOnItemClickListener(position -> superViewModel.getCourseList(data.get(position).beginYear, data.get(position).term));

            superViewModel.getCourseStatus.observe(getViewLifecycleOwner(), num -> {
                if (num == SuperViewModel.START_FETCH) {
                    dialog.setTitle(getString(R.string.warning));
                    dialog.setMessage(getString(R.string.ready_to_fetch_course));
                    dialog.show();
                } else if (num == SuperViewModel.FETCH_SUCCESS) {
                    dialog.dismiss();
                    if (getArguments() != null) {
                        Bundle arguments = getArguments();
                        String scheduleName;
                        if (!TextUtils.isEmpty(scheduleName = arguments.getString(ScheduleInitFragment.SCHEDULE_NAME))) {
                            List<Course> courseList = new ArrayList();
                            List<SuperCourse> superCourseData = superViewModel.getSuperCourseList().lessonList;
                            if (superCourseData.size() == 0) {
                                Dialog warnDialog = CommonDialogs.INSTANCE.getCommonDialog(activity(), getString(R.string.warning), "没有检测到该学期的课程,确定继续导入么？", () -> {
                                    parseCourse(scheduleName, courseList, superCourseData);
                                    return Unit.INSTANCE;
                                }, () -> {
                                    Toasts.toast("请重新登录获取最新数据！");
                                    Navigations.close(SuperCourseFetchFragment.this);
                                    return Unit.INSTANCE;
                                });
                                warnDialog.setCancelable(false);
                                warnDialog.show();
                            } else {
                                parseCourse(scheduleName, courseList, superCourseData);
                            }
                        }
                    } else {
                        Toasts.toast(getString(R.string.arg_exception));
                    }
                } else if (num == SuperViewModel.FETCH_FAIL) {
                    dialog.dismiss();
                    Toasts.toast(getString(R.string.fetch_fail_please_retry));
                } else if (num == SuperViewModel.TOKEN_FAIL) {
                    dialog.dismiss();
                    Toasts.toast("TOKEN失效，请重新重录或者退出App重试!");
                }
            });

        } catch (Exception ignored) {
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_super_course_term_list;
    }


    /**
     * 导入
     */
    private void parseCourse(String str, List<Course> courseList, List<SuperCourse> superCourseData) {
        scheduleId = scheduleViewModel.addSchedule(str, 24, 1, Schedule.IMPORT_WAY.SUPER_CN);
        Prefs.save(Constants.CUR_SCHEDULE, scheduleId);
        for (SuperCourse superCourse : superCourseData) {
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
            if (i > Constants.STAND_SESSION) {
                i = Constants.STAND_SESSION;
            }
            course.continuingSession = String.valueOf(i);
            StringBuilder sb2 = new StringBuilder();
            for (int i2 = 0; i2 < Constants.MAX_WEEK; i2++) {
                sb2.append(0);
            }
            //上课周处理
            for (String parseInt : superCourse.smartPeriod.split(" ")) {
                int parseInt2 = Integer.parseInt(parseInt);
                sb2.replace(parseInt2 - 1, parseInt2, String.valueOf(1));
            }
            course.classWeek = sb2.toString();


            courseList.add(course);
        }
        courseViewModel.saveCourseByDb(courseList, scheduleId);
        Toasts.toast("导入成功");
        Navigations.open(SuperCourseFetchFragment.this, R.id.action_superCourseFetchFragment_to_scheduleFragment);
    }
}
