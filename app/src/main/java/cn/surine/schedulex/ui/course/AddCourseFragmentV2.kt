package cn.surine.schedulex.ui.course

import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewPagerAdapter
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_course_v2.*
import java.util.*

class AddCourseFragmentV2 :BaseFragment(){
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var schedule: Schedule
    private lateinit var course: Course
    private var hasCourseData = false
    override fun layoutId() = R.layout.fragment_add_course_v2

    override fun onInit(parent: View?) {
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
        }
        schedule = scheduleViewModel.curSchedule
        when {
            arguments == null -> {
                Course().apply {
                    course = this
                    scheduleId = schedule.roomId.toLong()
                    color = Constants.COLOR_1[Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.size)]
                    id = "${scheduleId}@${System.currentTimeMillis()}"
                }
            }
            requireArguments().getString(BtmDialogs.COURSE_ID, "").isNotEmpty() -> {
                course = courseViewModel.getCourseById(requireArguments().getString(BtmDialogs.COURSE_ID))
                hasCourseData = true
                initCourseUi()
            }
            else -> {
                Toasts.toast(getString(R.string.get_course_fail))
                return
            }
        }

        //课程拷贝，需要重新赋值id
        if (arguments != null && requireArguments().getBoolean(ScheduleViewPagerAdapter.IS_COPY)) {
            hasCourseData = false
            course.apply {
                scheduleId = schedule.roomId.toLong()
                color = Constants.COLOR_1[Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.size)]
                id = "${course.scheduleId}@${System.currentTimeMillis()}"
            }
        }

        editCourseName.setOnClickListener {
            MIUIDialog(activity()).show {
                title(text = "编辑课程名称")
                input(prefill = if (hasCourseData) course.coureName else "", hint = if (!hasCourseData) courseNameSubTitle.text.toString() else "") { it, _ ->
                    course.coureName = it.toString()
                    courseNameSubTitle.text = it
                }
                positiveButton(text = "保存") { }
                negativeButton(text = "取消") { }
            }
        }
    }

    private fun initCourseUi() {

    }

}