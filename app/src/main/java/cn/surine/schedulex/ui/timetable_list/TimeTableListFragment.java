package cn.surine.schedulex.ui.timetable_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.data.entity.TimeTable;
import cn.surine.schedulex.databinding.FragmentTimetableListBinding;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_config.ScheduleConfigFragment;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:08
 */
public class TimeTableListFragment extends BaseBindingFragment<FragmentTimetableListBinding> {
    private BaseAdapter<TimeTable> adapter;
    private LinearLayoutManager layoutManager;
    private List<TimeTable> data = new ArrayList<>();
    private TimeTableViewModel timeTableViewModel;
    public static final String TIMETABLE_ID = "TIMETABLE_ID";
    private ScheduleViewModel scheduleViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_timetable_list;
    }


    @SuppressLint("StringFormatMatches")
    @Override
    protected void onInit(FragmentTimetableListBinding t) {

        Class[] classesForTimeTable = new Class[]{TimeTableRepository.class};
        Object[] argsForTimeTable = new Object[]{TimeTableRepository.abt.getInstance()};
        timeTableViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForTimeTable, argsForTimeTable)).get(TimeTableViewModel.class);

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);

        if (getArguments() == null || getArguments().getLong(ScheduleConfigFragment.SCHEDULE_ID, -1) == -1) {
            Toasts.toast(getString(R.string.app_error));
            Navigations.close(TimeTableListFragment.this);
        }

        data.clear();
        data.addAll(timeTableViewModel.getAllTimeTables());

        adapter = new BaseAdapter<>(data, R.layout.item_time_table_list, cn.surine.schedulex.BR.timetable);
        t.viewRecycler.setLayoutManager(layoutManager = new LinearLayoutManager(getActivity()));
        t.viewRecycler.setAdapter(adapter);
        t.viewRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    t.addTimeTable.shrink();
                } else {
                    t.addTimeTable.extend();
                }
            }
        });

        adapter.setOnItemClickListener(position -> {
            TimeTable timeTable = data.get(position);
            long scheduleId = getArguments().getLong(ScheduleConfigFragment.SCHEDULE_ID, -1);
            Schedule schedule = scheduleViewModel.getScheduleById(scheduleId);
            if (schedule != null) {
                if (schedule.maxSession > timeTable.getSessionNum()) {
                    Toasts.toast(getString(R.string.session_is_not_match, timeTable.getSessionNum(), schedule.maxSession));
                } else {
                    schedule.timeTableId = timeTable.roomId;
                    scheduleViewModel.updateSchedule(schedule);
                    Toasts.toast(getString(R.string.timetable_is_selected));
                    Navigations.close(TimeTableListFragment.this);
                }
            }
        });

        t.addTimeTable.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_timeTableListFragment_to_addTimeTableFragment));

        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.more_function) {
            @Override
            public void onClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_timetable, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.popup_menu_action_1:
                            Bundle bundle = new Bundle();
                            bundle.putInt(TIMETABLE_ID, data.get(position).roomId);
                            Navigations.open(TimeTableListFragment.this, R.id.action_timeTableListFragment_to_addTimeTableFragment, bundle);
                            break;
                        case R.id.popup_menu_action_2:
                            deleteTimeTable(position);
                            break;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });

    }

    private void deleteTimeTable(int position) {
        if (data.get(position).roomId == 0) {
            Toasts.toast(getString(R.string.is_ban_to_delete));
            return;
        }
        CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.are_you_sure_to_delete_timetable)
                , () -> {
                    timeTableViewModel.deleteTimeTableById(data.get(position).roomId);
                    Toasts.toast(getString(R.string.time_table_has_been_deleted));
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                }, null).show();
    }
}
