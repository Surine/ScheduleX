package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.surine.coursetableview.entity.BCourse;
import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.BoardCastSender;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.base.utils.Drawables;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.MySeekBarChangeListener;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleBinding;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.timer.TimerRepository;
import cn.surine.schedulex.ui.timer.TimerViewModel;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;
import cn.surine.schedulex.ui.view.custom.helper.ZoomOutPageTransformer;

/**
 * Intro：
 * 二期课表
 *
 * @author sunliwei
 * @date 2020-02-09 16:18
 */
public class ScheduleFragment extends BaseBindingFragment<FragmentScheduleBinding> {
    private ScheduleViewPagerAdapter scheduleViewPagerAdapter;
    private FragmentScheduleBinding globalT;
    private Schedule curSchedule;
    private int curViewPagerPosition;
    private PopupWindow popupWindow;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public int layoutId() {
        return R.layout.fragment_schedule;
    }


    @SuppressLint("StringFormatMatches")
    @Override
    protected void onInit(FragmentScheduleBinding t) {

        globalT = t;

        if (!Prefs.getBoolean(Constants.IS_FIRST, false)) {
            CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.first_toast), null, null).show();
            Prefs.save(Constants.IS_FIRST, true);
        }

        Class[] classesForCourse = new Class[]{CourseRepository.class};
        Object[] argsForCourse = new Object[]{CourseRepository.abt.getInstance()};
        CourseViewModel courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForCourse, argsForCourse)).get(CourseViewModel.class);

        Class[] classesForTimer = new Class[]{TimerRepository.class};
        Object[] argsForTimer = new Object[]{TimerRepository.abt.getInstance()};
        TimerViewModel timerViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimer, argsForTimer)).get(TimerViewModel.class);
        t.setTimer(timerViewModel);

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);


        curSchedule = scheduleViewModel.getCurSchedule();
        t.setSchedule(curSchedule);
        if (curSchedule == null) {
            return;
        }

        //当前周
        int currentWeek = curSchedule.curWeek();
        List<List<BCourse>> handleCourseList = new ArrayList<>();
        for (int i = 0; i < curSchedule.totalWeek; i++) {
            List<Course> dbData = courseViewModel.queryCourseByWeek(i + 1, curSchedule.roomId);
            List<BCourse> bCourseList = new ArrayList<>();
            for (Course course : dbData) {
                bCourseList.add(DataMaps.dataMappingByCourse(course));
            }
            handleCourseList.add(bCourseList);
        }

        scheduleViewPagerAdapter = new ScheduleViewPagerAdapter(handleCourseList, ScheduleFragment.this, curSchedule, currentWeek);

        t.viewpager.setAdapter(scheduleViewPagerAdapter);
        t.viewpager.setOffscreenPageLimit(1);
        t.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        t.viewpager.setCurrentItem(currentWeek - 1, true);
        t.viewpager.setPageTransformer(new ZoomOutPageTransformer());

        t.curWeekTv.setOnClickListener(v -> {
            View view = Uis.inflate(activity(),R.layout.view_change_week_quickly);
            popupWindow = new PopupWindow(view, Uis.dip2px(activity(),200), WindowManager.LayoutParams.WRAP_CONTENT);
            //设置外面可触
            popupWindow.setOutsideTouchable(true);
            //设置可触
            popupWindow.setFocusable(false);
            popupWindow.setBackgroundDrawable(Drawables.getDrawable(Color.WHITE,180,0,Color.WHITE));
            popupWindow.setTouchable(true);
            popupWindow.setElevation(8F);
            popupWindow.showAsDropDown(t.curWeekTv,20,30);

            SeekBar seekBar = view.findViewById(R.id.seekBar);
            TextView weekTv = view.findViewById(R.id.weekText);
            seekBar.setMax(curSchedule.totalWeek);
            seekBar.setProgress(curViewPagerPosition);
            weekTv.setText(String.valueOf(curViewPagerPosition + 1));
            seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    super.onProgressChanged(seekBar, progress, fromUser);
                    weekTv.setText(String.valueOf(progress + 1));
                    t.viewpager.setCurrentItem(progress);
                    if(popupWindow != null && popupWindow.isShowing()){
                        handler.removeCallbacks(runnable);
                        runnable = () -> popupWindow.dismiss();
                        handler.postDelayed(runnable,2000);
                    }
                }
            });
        });

        timerViewModel.curWeekStr.setValue("😁😁😁 " + getString(R.string.week, currentWeek));


        //显示空视图
        if (TextUtils.isEmpty(curSchedule.imageUrl)) {
            t.emptyView.setVisibility(handleCourseList.get(currentWeek - 1).size() != 0 ? View.GONE : View.VISIBLE);
        }
        t.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onPageSelected(int position) {
                curViewPagerPosition = position;
                timerViewModel.curWeekStr.setValue("😁😁😁 " + getString(R.string.week, (position + 1)));
                scheduleViewPagerAdapter.setWeek(position + 1);
                scheduleViewPagerAdapter.notifyItemChanged(position);
                if (TextUtils.isEmpty(curSchedule.imageUrl)) {
                    t.emptyView.setVisibility(handleCourseList.get(position).size() != 0 ? View.GONE : View.VISIBLE);
                }
            }
        });


        t.funcBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleFragment_to_ScheduleListFragment2));
        t.addCourse.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_scheduleFragment_to_addCourseFragment));

        t.title.setOnClickListener(v -> Toasts.toast("莫挨老子！😡😡😡"));

        if (!TextUtils.isEmpty(curSchedule.imageUrl)) {
            Glide.with(activity()).load(new File(curSchedule.imageUrl)).crossFade().into(t.background);
        }

        BoardCastSender.notifyWidget(activity());
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        activity().finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        //回到当前周，有个小bug可以临时通过这个解决一下
        globalT.viewpager.setCurrentItem(curSchedule.curWeek() - 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler != null){
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }
}
