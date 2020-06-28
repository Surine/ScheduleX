package cn.surine.schedulex.ui.login

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseBindingFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.databinding.FragmentLoginBinding
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule_init.ScheduleInitFragment
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs.getCommonDialog
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:22
 */
class LoginFragment : BaseBindingFragment<FragmentLoginBinding>() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var dialog: ProgressDialog
    override fun layoutId(): Int = R.layout.fragment_login

    override fun onInit(t: FragmentLoginBinding) {
        VmManager(this).apply {
            loginViewModel = vmLogin
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
        }
        dialog = ProgressDialog(activity()).apply {
            setCancelable(false)
        }
        t.data = loginViewModel
        loginViewModel.loginStatus.observe(this, Observer {
            when (it) {
                LoginViewModel.START_LOGIN -> {
                    dialog.apply {
                        setTitle(getString(R.string.warning))
                        setMessage(getString(R.string.ready_to_login_tust))
                        show()
                    }
                }
                LoginViewModel.LOGIN_SUCCESS -> {
                    courseViewModel.getCourseByNet()
                }
                LoginViewModel.LOGIN_FAIL -> {
                    dialog.dismiss()
                    Toasts.toast(getString(R.string.login_fail))
                }
            }
        })
        courseViewModel.getCourseStatus.observe(this, Observer {
            when (it) {
                CourseViewModel.START -> dialog.setMessage(getString(R.string.ready_to_get_schedule_list))
                CourseViewModel.SUCCESS -> {
                    dialog.dismiss()
                    if (arguments == null || requireArguments().getString(ScheduleInitFragment.SCHEDULE_NAME)?.isEmpty() != false) {
                        Toasts.toast(getString(R.string.arg_exception))
                    } else {
                        val scheduleId = scheduleViewModel.addSchedule(requireArguments().getString(ScheduleInitFragment.SCHEDULE_NAME), courseViewModel.totalWeek.value
                                ?: 24, courseViewModel.nowWeek.value ?: 1, Schedule.IMPORT_WAY.JW)
                        Prefs.save(Constants.CUR_SCHEDULE, scheduleId)
                        courseViewModel.mMutableCourses.value?.let { courseList ->
                            courseList.forEach { course ->
                                course.scheduleId = scheduleId
                                course.id = "${course.scheduleId}@${course.id}"
                            }
                            courseViewModel.saveCourseByDb(courseList, scheduleId)
                            Navigations.open(this, R.id.scheduleFragment)
                            Toasts.toast(getString(R.string.handle_success))
                        }
                    }
                }
                CourseViewModel.FAIL -> {
                    dialog.dismiss()
                    Toasts.toast(getString(R.string.get_fail))
                }
            }
        })
        loginTip.setOnClickListener { getCommonDialog(activity(), getString(R.string.warning), getString(R.string.welcome_to_use)).show() }
    }

}