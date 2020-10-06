package cn.surine.schedulex.super_import.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;

import java.io.Serializable;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.BaseBindingFragment;
import cn.surine.schedulex.base.utils.InstanceFactory;
import cn.surine.schedulex.base.utils.Navigations;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.databinding.FragmentLoginSuperBinding;
import cn.surine.schedulex.super_import.viewmodel.SuperRepository;
import cn.surine.schedulex.super_import.viewmodel.SuperViewModel;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.course.CourseViewModel;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.schedule.ScheduleViewModel;
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs;
import kotlin.Unit;

import static cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment.SCHEDULE_NAME;

public class SuperLoginFragment extends BaseBindingFragment<FragmentLoginSuperBinding> {
    private CourseViewModel courseViewModel;
    private ProgressDialog dialog;
    private long scheduleId;
    private ScheduleViewModel scheduleViewModel;
    private SuperViewModel superViewModel;

    public static final String TERM_DATA = "term_data";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置共享动画
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_login_super;
    }

    @Override
    public void onInit(FragmentLoginSuperBinding fragmentLoginSuperBinding) {
        superViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{SuperRepository.class}, new Object[]{SuperRepository.abt.getInstance()})).get(SuperViewModel.class);
        fragmentLoginSuperBinding.setData(this.superViewModel);
        courseViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{CourseRepository.class}, new Object[]{CourseRepository.abt.getInstance()})).get(CourseViewModel.class);
        scheduleViewModel = ViewModelProviders.of(this, InstanceFactory.getInstance(new Class[]{ScheduleRepository.class}, new Object[]{ScheduleRepository.abt.getInstance()})).get(ScheduleViewModel.class);
        dialog = new ProgressDialog(activity());
        dialog.setCancelable(false);
        superViewModel.loginStatus.observe(getViewLifecycleOwner(), num -> {
            if (num == SuperViewModel.START_LOGIN) {
                dialog.setTitle(getString(R.string.warning));
                dialog.setMessage(getString(R.string.ready_to_login_super_class));
                dialog.show();
            } else if (num == SuperViewModel.LOGIN_SUCCESS) {
                dialog.dismiss();
                //跳转学期列表页。
                Bundle bundle = new Bundle();
                bundle.putString(SCHEDULE_NAME, getArguments().getString(SCHEDULE_NAME));
                bundle.putSerializable(TERM_DATA, (Serializable) (superViewModel.userData.getValue().student.attachmentBO.myTermList));
                Navigations.open(this, R.id.action_superLoginFragment_to_superCourseFetchFragment, bundle);
                superViewModel.loginStatus.setValue(-1);
            } else if (num == SuperViewModel.LOGIN_FAIL) {
                dialog.dismiss();
                Toasts.toast(getString(R.string.login_fail));
            }
        });

        fragmentLoginSuperBinding.superTip.setOnClickListener(view -> CommonDialogs.INSTANCE.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.super_tip),()-> Unit.INSTANCE,()-> Unit.INSTANCE));
        fragmentLoginSuperBinding.superTipLogo.setOnClickListener(v -> fragmentLoginSuperBinding.superTip.performClick());

    }

}
