package cn.surine.schedulex.ui.schedule_list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseAdapter;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.databinding.FragmentScheduleListBinding;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;

public class ScheduleListFragment extends BaseBindingFragment<FragmentScheduleListBinding> {

    public static final String SCHEDULE_ID = "schedule_id";
    private List<Schedule> data;


    @Override
    public int layoutId() {
        return R.layout.fragment_schedule_list;
    }


    @Override
    protected void onInit(FragmentScheduleListBinding t) {

        Class[] classesForSchedule = new Class[]{ScheduleRepository.class};
        Object[] argsForSchedule = new Object[]{ScheduleRepository.abt.getInstance()};
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classesForSchedule, argsForSchedule)).get(ScheduleViewModel.class);


        data = scheduleViewModel.getSchedules();
        BaseAdapter<Schedule> adapter = new BaseAdapter<>(data, R.layout.item_schedule_view, cn.surine.schedulex.BR.schedule);
        RecyclerView recyclerview = (RecyclerView) t.viewRecycler;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity());
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Long scheduleId = (long) data.get(position).roomId;
            Prefs.save(Constants.CUR_SCHEDULE,scheduleId);
            adapter.notifyDataSetChanged();
        });


        adapter.setOnItemElementClickListener(new BaseAdapter.OnItemElementClickListener(R.id.editSchedule) {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(SCHEDULE_ID, data.get(position).roomId);
                Navigations.open(ScheduleListFragment.this, R.id.action_ScheduleListFragment_to_scheduleConfigFragment,bundle);
            }
        });


        t.addSchedule.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_ScheduleListFragment_to_scheduleInitFragment));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ScheduleListViewModel mViewModel = ViewModelProviders.of(this).get(ScheduleListViewModel.class);
    }

}
