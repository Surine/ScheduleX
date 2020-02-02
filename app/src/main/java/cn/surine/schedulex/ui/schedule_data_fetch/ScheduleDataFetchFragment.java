package cn.surine.schedulex.ui.schedule_data_fetch;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Objs;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.databinding.FragmentDataFetchBinding;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-23 20:54
 */
public class ScheduleDataFetchFragment extends BaseBindingFragment<FragmentDataFetchBinding> {

    ScheduleDataFetchViewModel dataFetchViewModel;
    ScheduleViewModel scheduleViewModel;

    @Override
    public int layoutId() {
        return R.layout.fragment_data_fetch;
    }

    @Override
    protected void onInit(FragmentDataFetchBinding t) {
        dataFetchViewModel = ViewModelProviders.of(this).get(ScheduleDataFetchViewModel.class);
        t.setData(dataFetchViewModel);

        Class[] classes = new Class[]{ScheduleRepository.class};
        Object[] args = new Object[]{ScheduleRepository.abt.getInstance()};
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(classes, args)).get(ScheduleViewModel.class);

        String scheduleName = Objs.notNull(getArguments()) ? getArguments().getString(ScheduleInitFragment.SCHEDULE_NAME) : "UNKNOWN";
        t.scheduleTitle.setText(scheduleName);

        Bundle bundle = new Bundle();
        bundle.putString(ScheduleInitFragment.SCHEDULE_NAME, scheduleName);
        t.loginJw.setOnClickListener(v -> Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_loginFragment, bundle));
        t.importJson.setOnClickListener(v -> Toasts.toast("Json"));
        t.importExcel.setOnClickListener(v -> Toasts.toast("excel"));
        t.scanQrCode.setOnClickListener(v -> Toasts.toast("qr"));
        t.other.setOnClickListener(v -> Toasts.toast("other"));
        t.skip.setOnClickListener(v -> {
            Prefs.save(Constants.CUR_SCHEDULE,scheduleViewModel.addSchedule(scheduleName, 24, 1));
            Navigations.open(ScheduleDataFetchFragment.this, R.id.action_dataFetchFragment_to_dailyFragment);
        });
    }
}
