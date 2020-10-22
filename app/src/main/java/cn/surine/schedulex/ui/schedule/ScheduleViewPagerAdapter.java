package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import cn.surine.coursetableview.entity.BCourse;
import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.coursetableview.view.CourseTableView;
import cn.surine.coursetableview.view.DataConfig;
import cn.surine.coursetableview.view.UIConfig;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseFragment;
import cn.surine.schedulex.base.interfaces.Call;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs;
import kotlin.Unit;

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
    private CourseViewModel courseViewModel;
    private Call dataSetUpdateCall;
    private HashMap<String, BCourse> selectCourse = new HashMap<>();
    public static final String IS_COPY = "IS_COPY";
    int[] raws = new int[]{
            R.raw.a4, R.raw.a4m, R.raw.b4, R.raw.c4,
            R.raw.c4m, R.raw.d4, R.raw.d4m, R.raw.e4,
            R.raw.f4, R.raw.f4m, R.raw.g4, R.raw.g4m,
    };


    public ScheduleViewPagerAdapter(List<List<BCourse>> courseList, BTimeTable timeTable, BaseFragment baseFragment, Schedule schedule, int week, CourseViewModel courseViewModel) {
        this.courseList = courseList;
        this.baseFragment = baseFragment;
        this.schedule = schedule;
        this.week = week;
        this.timeTable = timeTable;
        this.courseViewModel = courseViewModel;
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
            uiConfig.setSectionViewWidth(Uis.dip2px(App.context, 40));
        } else {
            uiConfig.setMaxClassDay(5);
            uiConfig.setItemTextSize(13);
            uiConfig.setItemTopMargin(10);
            uiConfig.setItemSideMargin(8);
            uiConfig.setSectionViewWidth(Uis.dip2px(App.context, 50));
        }
        uiConfig.setShowTimeTable(schedule.isShowTime);
        uiConfig.setMaxSection(schedule.maxSession);
        uiConfig.setShowNotCurWeekCourse(false);
        uiConfig.setSectionHeight(Uis.dip2px(App.context, schedule.itemHeight));
        uiConfig.setChooseWeekColor(App.context.getResources().getColor(R.color.colorPrimary));
        uiConfig.setColorUI(schedule.lightText ? UIConfig.LIGHT : UIConfig.DARK);
        uiConfig.setMaxHideCharLimit(schedule.maxHideCharLimit);
        uiConfig.setTextAlignFlag(schedule.textAlignFlag);
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
        if (selectCourse != null) {
            selectCourse.clear();
        }

        DataConfig dataConfig = new DataConfig();
        dataConfig.setCurTermStartDate(schedule.termStartDate)
                .setCourseList(courseList.get(position))
                .setTimeTable(timeTable)
                .setCurrentWeek(week);


        holder.courseTableView.update(uiConfig, dataConfig);

        holder.courseTableView.setmClickCourseItemListener((view, list, itemPosition, isThisWeek) -> {
            if (!Prefs.getBoolean(Constants.EGG, false)) {
                Course course = DataMaps.dataMappingByBCourse(courseList.get(position).get(itemPosition));
                BtmDialogs.INSTANCE.showCourseInfoBtmDialog(baseFragment, course, schedule.alphaForCourseItem, courseViewModel, () -> {
                    if (dataSetUpdateCall != null) {
                        dataSetUpdateCall.back();
                    }
                });
            } else {
                MediaPlayer mp = MediaPlayer.create(baseFragment.activity(), raws[new Random().nextInt(12)]);
                mp.start();
            }
        });

        holder.courseTableView.setLongClickCourseItemListener((view, list, itemPosition, isThisWeek) -> {
            Bundle bundle = new Bundle();
            bundle.putString(COURSE_ID, list.get(itemPosition).getId());
            if (view.getTag(view.getId()) == null || !(Boolean) view.getTag(view.getId())) {
                view.setTag(view.getId(), true);
                view.animate().scaleX(0.8F).scaleY(0.8F).setInterpolator(new OvershootInterpolator());
                selectCourse.put(list.get(itemPosition).getId(), list.get(itemPosition));
            } else {
                view.animate().scaleX(1F).scaleY(1F).setInterpolator(new OvershootInterpolator());
                view.setTag(view.getId(), false);
                selectCourse.remove(list.get(itemPosition).getId());
            }
            ScheduleFragment scheduleFragment = (ScheduleFragment) baseFragment;
            try {
                if (selectCourse.size() > 0) {
                    Uis.show(scheduleFragment.getDataBinding().courseOp);
                    scheduleFragment.getDataBinding().courseOp.setOnClickListener(v -> {
                        if (selectCourse.size() > 0) {
                            showSelectCourseDialog(position);
                        } else {
                            Uis.hide(scheduleFragment.getDataBinding().courseOp);
                        }
                    });
                } else {
                    Uis.hide(scheduleFragment.getDataBinding().courseOp);
                }
            } catch (Exception e) {
                CrashReport.postCatchedException(new RuntimeException("获取DataBinding失败" + e.getMessage()));
            }

        });

    }

    /**
     * 展示批量操作弹窗
     *
     * @param position
     */
    private void showSelectCourseDialog(int position) {
        View view = Uis.inflate(baseFragment.activity(), R.layout.view_select_course_op);
        BtmDialogs.INSTANCE.getBaseConfig(baseFragment.activity(), view, (targetView, bottomSheetDialog) -> {
            RecyclerView recyclerView = targetView.findViewById(R.id.recyclerview);
            List<BCourse> bCourses = new ArrayList<>();
            for (String key : selectCourse.keySet()) {
                bCourses.add(selectCourse.get(key));
            }
            BaseAdapter<BCourse> adapter = new BaseAdapter<>(bCourses, R.layout.item_select_course_list_ui, BR.bCourse);
            recyclerView.setLayoutManager(new LinearLayoutManager(baseFragment.activity(), RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.copy_course) {
                @Override
                public void onClick(View v, int position1) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BtmDialogs.COURSE_ID, bCourses.get(position1).getId());
                    bundle.putBoolean(IS_COPY, true);
                    Navigations.open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, bundle);
                    bottomSheetDialog.dismiss();
                }
            });

            //delete
            Button vDelete = targetView.findViewById(R.id.delete);
            CheckBox vCheckBox = targetView.findViewById(R.id.checkBox);
            vCheckBox.setText("仅影响当前周(第" + (position + 1) + "周)");
            //删除course
            vDelete.setOnClickListener(v -> Toasts.toast("操作不可逆，确认操作请长按此按钮！"));
            vDelete.setOnLongClickListener(v -> {
                if (vCheckBox.isChecked()) {
                    for (BCourse bc : bCourses) {
                        courseViewModel.deleteCourseWeekByCourseId(bc.getId(), position + 1);
                    }
                } else {
                    for (BCourse bc : bCourses) {
                        courseViewModel.deleteByCourseId(bc.getId());
                    }
                }
                //通知主页刷新数据
                if (dataSetUpdateCall != null) {
                    dataSetUpdateCall.back();
                }
                Toasts.toast(App.context.getResources().getString(R.string.handle_success));
                bottomSheetDialog.dismiss();
                Uis.hide(((ScheduleFragment) baseFragment).getDataBinding().courseOp);
                return true;
            });
            Button vChooseDate = targetView.findViewById(R.id.chooseDate);
            CalendarView vCalendarView = targetView.findViewById(R.id.calendar);
            vChooseDate.setOnClickListener(v -> Toasts.toast("操作不可逆，确认操作请长按此按钮！"));
            final String[] dateStr = new String[1];
            dateStr[0] = Dates.getDateStringByStamp(vCalendarView.getDate(), Dates.yyyyMMdd);
            vCalendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                dateStr[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
                Toasts.toast("当前目标日期：" + dateStr[0]);
            });
            vChooseDate.setOnLongClickListener(v -> {
                //清理本周，每清理一个，都要在对应日期中加一个
                for (BCourse bc : bCourses) {
                    courseViewModel.deleteCourseWeekByCourseId(bc.getId(), position + 1);
                    Course course = DataMaps.dataMappingByBCourse(bc);
                    //获取
                    String weekInfo = getDateMappingWeek(dateStr[0], schedule.termStartDate);
                    if (TextUtils.isEmpty(weekInfo)) {
                        Toasts.toast("不得移动至学期外！");
                        return true;
                    }
                    course.classWeek = weekInfo;
                    try {
                        course.classDay = String.valueOf(Dates.getWeekDay(dateStr[0]));
                    } catch (Exception e) {
                        course.classDay = "1";
                    }
                    //id做一个新的
                    StringBuilder sb = new StringBuilder();
                    sb.append(course.scheduleId);
                    sb.append("@");
                    sb.append(UUID.randomUUID());
                    sb.append(System.currentTimeMillis());
                    course.id = sb.toString();
                    courseViewModel.insert(course);
                }
                //通知主页刷新数据
                if (dataSetUpdateCall != null) {
                    dataSetUpdateCall.back();
                }
                Toasts.toast(App.context.getResources().getString(R.string.handle_success));
                bottomSheetDialog.dismiss();
                Uis.hide(((ScheduleFragment) baseFragment).getDataBinding().courseOp);
                return true;
            });
            return Unit.INSTANCE;
        });
    }

    /**
     * 获取选中日期对应的周信息
     */
    private String getDateMappingWeek(String s, String termStartDate) {
        if (Dates.compareDate(s, termStartDate) <= 0) {
            return null;
        }
        int week = (Dates.getDateDif(s, termStartDate) / 7) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Constants.MAX_WEEK; i++) {
            stringBuilder.append(0);
        }
        stringBuilder.replace(week - 1, week, "1");
        return stringBuilder.toString();
    }


    public Call getDataSetUpdateCall() {
        return dataSetUpdateCall;
    }

    public void setDataSetUpdateCall(Call dataSetUpdateCall) {
        this.dataSetUpdateCall = dataSetUpdateCall;
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
