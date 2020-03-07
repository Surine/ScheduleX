package cn.surine.schedulex.ui.timetable_list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.data.entity.TimeTable;
import cn.surine.schedulex.databinding.FragmentTimetableListBinding;

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

    @Override
    public int layoutId() {
        return R.layout.fragment_timetable_list;
    }


    @Override
    protected void onInit(FragmentTimetableListBinding t) {
        data.add(new TimeTable("默认"));
        data.add(new TimeTable("导入1"));
        data.add(new TimeTable("导入2"));
        data.add(new TimeTable("导入3"));
        data.add(new TimeTable("导入3"));
        data.add(new TimeTable("导入3"));
        data.add(new TimeTable("导入3"));
        data.add(new TimeTable("导入3"));
        data.add(new TimeTable("导入3"));

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

        adapter.setOnItemClickListener(position -> Navigations.open(TimeTableListFragment.this, R.id.action_timeTableListFragment_to_addTimeTableFragment));
    }
}
