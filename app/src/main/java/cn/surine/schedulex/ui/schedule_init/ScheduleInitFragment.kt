package cn.surine.schedulex.ui.schedule_init

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.BaseBindingFragment
import cn.surine.schedulex.base.utils.Navigations.open
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.databinding.FragmentScheduleInitBinding
import cn.surine.schedulex.third_parse.Shell
import kotlinx.android.synthetic.main.fragment_schedule_init.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 16:06
 */
class ScheduleInitFragment : BaseBindingFragment<FragmentScheduleInitBinding>() {
    private lateinit var scheduleInitViewModel: ScheduleInitViewModel

    companion object {
        const val SCHEDULE_NAME: String = "SCHEDULE_NAME"
    }

    override fun layoutId(): Int = R.layout.fragment_schedule_init

    override fun onInit(t: FragmentScheduleInitBinding) {
        scheduleInitViewModel = ViewModelProviders.of(this)[ScheduleInitViewModel::class.java]
        t.data = scheduleInitViewModel
        welcome.setOnClickListener {
            if (scheduleInitViewModel.scheduleName.value != null) {
                open(this, R.id.action_scheduleInitFragment_to_dataFetchFragment, Bundle().apply {
                    putString(SCHEDULE_NAME, scheduleInitViewModel.scheduleName.value)
                })
            } else {
                toast(getString(R.string.param_empty))
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        activity().finish()
    }

}