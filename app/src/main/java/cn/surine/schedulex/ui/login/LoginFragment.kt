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
import cn.surine.schedulex.ui.schedule_import_pro.core.ParseDispatcher.UNIVERSITY
import cn.surine.schedulex.ui.schedule_import_pro.model.RemoteUniversity
import cn.surine.schedulex.ui.schedule_import_pro.viewmodel.ScheduleDataFetchViewModel
import com.peanut.sdk.miuidialog.MIUIDialog
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
    private lateinit var dataFetchViewModel: ScheduleDataFetchViewModel
    private lateinit var dialog: ProgressDialog
    override fun layoutId(): Int = R.layout.fragment_login

    override fun onInit(t: FragmentLoginBinding) {
        VmManager(this).apply {
            loginViewModel = vmLogin
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
            dataFetchViewModel = vmScheduleFetch
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
                    val scheduleId = scheduleViewModel.addSchedule("我的课程表", courseViewModel.totalWeek.value
                            ?: 24, courseViewModel.nowWeek.value ?: 1, Schedule.IMPORT_WAY.JW)
                    Prefs.save(Constants.CUR_SCHEDULE, scheduleId)
                    courseViewModel.mMutableCourses.value?.let { courseList ->
                        courseList.forEach { course ->
                            course.scheduleId = scheduleId
                            course.id = "${course.scheduleId}@${course.id}"
                        }
                        courseViewModel.saveCourseByDb(courseList, scheduleId)
                        arguments?.let { bundle ->
                            dataFetchViewModel.uploadFetchSuccess(bundle.getSerializable(UNIVERSITY) as RemoteUniversity)
                        }
                        Navigations.open(this, R.id.scheduleFragment)
                        Toasts.toast(getString(R.string.handle_success))
                    }
                }
                CourseViewModel.FAIL -> {
                    dialog.dismiss()
                    Toasts.toast(getString(R.string.get_fail))
                }
            }
        })
        loginTip.setOnClickListener {
            MIUIDialog(activity()).show {
                title(res = R.string.warning)
                message(res = R.string.welcome_to_use)
                positiveButton(text = "确定") { this.cancel() }
                negativeButton(text = "取消") { this.cancel() }
            }
        }
    }

}