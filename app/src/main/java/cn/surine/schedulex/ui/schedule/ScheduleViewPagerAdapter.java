package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.surine.coursetableview.entity.BCourse;
import cn.surine.coursetableview.view.CourseTableView;
import cn.surine.coursetableview.view.DataConfig;
import cn.surine.coursetableview.view.UIConfig;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;

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


    public ScheduleViewPagerAdapter(List<List<BCourse>> courseList, BaseFragment baseFragment, Schedule schedule, int week) {
        this.courseList = courseList;
        this.baseFragment = baseFragment;
        this.schedule = schedule;
        this.week = week;
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
            uiConfig.setSectionHeight(Uis.dip2px(App.context, 54));
            uiConfig.setItemTopMargin(5);
            uiConfig.setItemSideMargin(3);
        } else {
            uiConfig.setMaxClassDay(5);
            uiConfig.setItemTextSize(14);
            uiConfig.setSectionHeight(Uis.dip2px(App.context, 64));
            uiConfig.setItemTopMargin(10);
            uiConfig.setItemSideMargin(12);
        }
        uiConfig.setShowCurWeekCourse(false);
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
                .setCurrentWeek(week);


        holder.courseTableView.update(uiConfig, dataConfig);

        holder.courseTableView.setmClickCourseItemListener((list, itemPosition, isThisWeek) -> {
            Course course = DataMaps.dataMappingByBCourse(courseList.get(position).get(itemPosition));
            if (course == null) {
                return;
            }
            BtmDialogs.showCourseInfoBtmDialog(baseFragment, course);
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
