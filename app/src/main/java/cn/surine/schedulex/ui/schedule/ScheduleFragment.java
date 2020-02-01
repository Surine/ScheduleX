package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Strs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.timer.TimerRepository;
import cn.surine.schedulex.ui.timer.TimerViewModel;

public class ScheduleFragment extends BaseBindingFragment<FragmentScheduleBinding> {

    private CourseViewModel courseViewModel;
    private TimerViewModel timerViewModel;
    private ScheduleViewModel scheduleViewModel;


    @Override
    public int layoutId() {
        return R.layout.fragment_schedule;
    }


    @SuppressLint("StringFormatMatches")
    @Override
    protected void onInit(FragmentScheduleBinding t) {

        if (!Prefs.getBoolean(Constants.IS_FIRST, false)) {
            Prefs.save(Constants.IS_FIRST, true);
        }

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        Class[] classesForTimer = new Class[]{TimerRepository.class};
        Object[] argsForTimer = new Object[]{TimerRepository.abt.getInstance()};
        timerViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimer, argsForTimer)).get(TimerViewModel.class);
        t.setTimer(timerViewModel);

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);

        Schedule curSchedule = scheduleViewModel.getCurSchedule();
        if (curSchedule == null) {
            return;
        }

        //当前周
        int currentWeek = curSchedule.curWeek();
        List<List<Course>> unHandleCourseList = new ArrayList<>();
        for (int i = 0; i < curSchedule.totalWeek; i++) {
            unHandleCourseList.add(courseViewModel.queryCourseByWeek(i + 1,curSchedule.roomId));
        }
        List<List<Course>> cl = scheduleViewModel.getAllCourseUiData(unHandleCourseList, curSchedule);
        ScheduleViewpagerAdapter scheduleViewpagerAdapter = new ScheduleViewpagerAdapter(cl, ScheduleFragment.this);
        scheduleViewpagerAdapter.setOnScrollBindListener(position -> t.scheduleSlideBar.scrollBy(0,position));
        t.scheduleSlideBar.setOnTouchListener((v, event) -> true);

        t.viewpager.setAdapter(scheduleViewpagerAdapter);
        t.viewpager.setOffscreenPageLimit(1);
        t.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        t.viewpager.setCurrentItem(currentWeek - 1,true);
        timerViewModel.curWeekStr.setValue("😁😁😁 "+getString(R.string.week,currentWeek));


        if(isAllNull(cl.get(0))){
//            t.emptyView.setVisibility(View.VISIBLE);
        }
        t.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onPageSelected(int position) {
                timerViewModel.curWeekStr.setValue("😁😁😁 "+getString(R.string.week,(position + 1)));
                t.scheduleSlideBar.scrollBy(0,0);
                if(isAllNull(cl.get(position))){
//                    t.emptyView.setVisibility(View.VISIBLE);
                }else{
//                    t.emptyView.setVisibility(View.GONE);
                }
            }
        });

        t.funcBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dailyFragment_to_funcFragment));
        t.addCourse.setOnClickListener(v -> Toasts.toast(getResources().getString(R.string.add_course)));
        t.title.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dailyFragment_to_aboutFragment));
    }

    private boolean isAllNull(List<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if(courseList.get(i) != null){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
