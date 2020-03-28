package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import cn.surine.coursetableview.entity.BCourse;
import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.coursetableview.view.CourseTableView;
import cn.surine.coursetableview.view.DataConfig;
import cn.surine.coursetableview.view.UIConfig;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;

import static cn.surine.schedulex.ui.view.custom.helper.BtmDialogs.COURSE_ID;

/**
 * Intro：
 * 课表二期
 *
 * @author sunliwei
 * @date 2020-02-09 16:38
 */
public class ScheduleViewPagerAdapter extends RecyclerView.Adapter<ScheduleViewPagerAdapter.ViewPagerViewHolder> {

    private int week;
    private List<List<BCourse>> courseList;
    private BaseFragment baseFragment;
    private Schedule schedule;
    private UIConfig uiConfig;
    private BTimeTable timeTable;
    int[] raws = new int[]{
            R.raw.a4,R.raw.a4m,R.raw.b4,R.raw.c4,
            R.raw.c4m,R.raw.d4,R.raw.d4m,R.raw.e4,
            R.raw.f4,R.raw.f4m,R.raw.g4,R.raw.g4m,
    };


    public ScheduleViewPagerAdapter(List<List<BCourse>> courseList, BTimeTable timeTable, BaseFragment baseFragment, Schedule schedule, int week) {
        this.courseList = courseList;
        this.baseFragment = baseFragment;
        this.schedule = schedule;
        this.week = week;
        this.timeTable = timeTable;
        initUI();
    }

    /**
     * 加载一遍默认UI
     */
    private void initUI() {
        uiConfig = new UIConfig();
        if (schedule.isShowWeekend) {
            uiConfig.setMaxClassDay(7);
            uiConfig.setItemTextSize(12);
            uiConfig.setItemTopMargin(5);
            uiConfig.setItemSideMargin(3);
            uiConfig.setSectionViewWidth(Uis.dip2px(App.context, 45));
        } else {
            uiConfig.setMaxClassDay(5);
            uiConfig.setItemTextSize(14);
            uiConfig.setItemTopMargin(10);
            uiConfig.setItemSideMargin(8);
            uiConfig.setSectionViewWidth(Uis.dip2px(App.context, 50));
        }
        uiConfig.setShowTimeTable(schedule.isShowTime);
        uiConfig.setShowCurWeekCourse(false);
        uiConfig.setMaxSection(schedule.maxSession);
        uiConfig.setSectionHeight(Uis.dip2px(App.context, schedule.itemHeight));
        uiConfig.setChooseWeekColor(App.context.getResources().getColor(R.color.colorPrimary));
        uiConfig.setColorUI(schedule.lightText ? UIConfig.LIGHT : UIConfig.DARK);
    }

    public void setWeek(int week) {
        this.week = week;
    }

    @NonNull
    @Override
    public ScheduleViewPagerAdapter.ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_table, parent, false);
        return new ScheduleViewPagerAdapter.ViewPagerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewPagerAdapter.ViewPagerViewHolder holder, int position) {

        DataConfig dataConfig = new DataConfig();
        dataConfig.setCurTermStartDate(schedule.termStartDate)
                .setCourseList(courseList.get(position))
                .setTimeTable(timeTable)
                .setCurrentWeek(week);


        holder.courseTableView.update(uiConfig, dataConfig);

        holder.courseTableView.setmClickCourseItemListener((list, itemPosition, isThisWeek) -> {
            if(!Prefs.getBoolean(Constants.EGG,false)){
                Course course = DataMaps.dataMappingByBCourse(courseList.get(position).get(itemPosition));
                if (course == null) {
                    return;
                }
                BtmDialogs.showCourseInfoBtmDialog(baseFragment, course);
            }else{
                MediaPlayer mp = MediaPlayer.create(baseFragment.activity(),raws[new Random().nextInt(12)]);
                mp.start();
            }
        });

        holder.courseTableView.setLongClickCourseItemListener((list, itemPosition, isThisWeek) -> {
            Bundle bundle = new Bundle();
            bundle.putString(COURSE_ID, list.get(itemPosition).getId());
            Navigations.open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, bundle);
        });

    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        CourseTableView courseTableView;

        ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTableView = itemView.findViewById(R.id.courseTableView);
        }
    }
}
