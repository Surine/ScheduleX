package cn.surine.schedulex.ui.schedule_init;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.databinding.FragmentScheduleInitBinding;

public class ScheduleInitFragment extends BaseBindingFragment<FragmentScheduleInitBinding> {

    private ScheduleInitViewModel scheduleInitViewModel;

    public static final String SCHEDULE_NAME = "SCHEDULE_NAME";

    public static ScheduleInitFragment newInstance() {
        return new ScheduleInitFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_schedule_init;
    }

    @Override
    protected void onInit(FragmentScheduleInitBinding t) {
        scheduleInitViewModel = ViewModelProviders.of(this).get(ScheduleInitViewModel.class);
        t.setData(scheduleInitViewModel);
        t.welcome.setOnClickListener(v -> {
            if (TextUtils.isEmpty(scheduleInitViewModel.scheduleName.getValue())) {
                Toasts.toast(getString(R.string.param_empty));
                return;
            }
            //传递数据给DataFetch
            Bundle bundle = new Bundle();
            bundle.putString(SCHEDULE_NAME, scheduleInitViewModel.scheduleName.getValue());
            Navigations.open(ScheduleInitFragment.this, R.id.action_scheduleInitFragment_to_dataFetchFragment, bundle);
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        activity().finish();
    }
}
