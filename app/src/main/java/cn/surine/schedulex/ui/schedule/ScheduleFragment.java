package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.timer.TimerRepository;
import cn.surine.schedulex.ui.timer.TimerViewModel;
import cn.surine.schedulex.ui.view.custom.helper.ZoomOutPageTransformer;

public class ScheduleFragment extends BaseBindingFragment<FragmentScheduleBinding> {

    private TimerViewModel timerViewModel;


    @Override
    public int layoutId() {
        return R.layout.fragment_schedule;
    }


    @SuppressLint("StringFormatMatches")
    @Override
    protected void onInit(FragmentScheduleBinding t) {

        if (Prefs.getBoolean(Constants.IS_FIRST, false)) {
            Prefs.save(Constants.IS_FIRST, true);
        }

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        CourseViewModel courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        Class[] classesForTimer = new Class[]{TimerRepository.class};
        Object[] argsForTimer = new Object[]{TimerRepository.abt.getInstance()};
        timerViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimer, argsForTimer)).get(TimerViewModel.class);
        t.setTimer(timerViewModel);

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);




        Schedule curSchedule = scheduleViewModel.getCurSchedule();
        if (curSchedule == null) {
            return;
        }
        //当前周
        int currentWeek = curSchedule.curWeek();
        List<List<Course>> unHandleCourseList = new ArrayList<>();
        for (int i = 0; i < curSchedule.totalWeek; i++) {
            List<Course> dbData = courseViewModel.queryCourseByWeek(i + 1, curSchedule.roomId);
            unHandleCourseList.add(dbData);
        }
        List<List<Course>> cl = scheduleViewModel.getAllCourseUiData(unHandleCourseList, curSchedule);
        ScheduleViewpagerAdapter scheduleViewpagerAdapter = new ScheduleViewpagerAdapter(cl, ScheduleFragment.this);
        scheduleViewpagerAdapter.setOnScrollBindListener(position -> t.scheduleSlideBar.scrollBy(0, position));
        t.scheduleSlideBar.setOnTouchListener((v, event) -> true);

        t.viewpager.setAdapter(scheduleViewpagerAdapter);
        t.viewpager.setOffscreenPageLimit(1);
        t.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        t.viewpager.setCurrentItem(currentWeek - 1, true);
        t.viewpager.setPageTransformer(new ZoomOutPageTransformer());

        t.curWeekTv.setOnClickListener(v -> t.viewpager.setCurrentItem(currentWeek - 1));
        timerViewModel.curWeekStr.setValue("😁😁😁 " + getString(R.string.week, currentWeek));
        t.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onPageSelected(int position) {
                timerViewModel.curWeekStr.setValue("😁😁😁 " + getString(R.string.week, (position + 1)));
                t.scheduleSlideBar.scrollBy(0, 0);
            }
        });



        t.funcBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dailyFragment_to_funcFragment));
        t.addCourse.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dailyFragment_to_addCourseFragment));
        t.title.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dailyFragment_to_aboutFragment));

        if (!TextUtils.isEmpty(curSchedule.imageUrl)) {
            Uri uri = Uri.parse(curSchedule.imageUrl);
            Glide.with(activity()).load(uri).into(t.background);
            configPaletteTextColor(t);
        }

    }


    /**
     * 配置文字颜色
     */
    private void configPaletteTextColor(FragmentScheduleBinding b) {
//        Bitmap bitmap =((BitmapDrawable)b.background.getDrawable()).getBitmap();
//        Palette.from(bitmap).generate(palette -> Objs.isNullCall(palette.getLightMutedSwatch(), () -> {
//            int color = palette.getLightMutedSwatch().getRgb();
//            if(ColorUtils.calculateLuminance(color) > 0.5){
//                b.title.setTextColor(Color.BLACK);
//            }else{
//                b.title.setTextColor(Color.WHITE);
//            }
//        }));
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        activity().finish();
    }
}
