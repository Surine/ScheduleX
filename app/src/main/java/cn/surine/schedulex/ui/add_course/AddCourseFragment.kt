package cn.surine.schedulex.ui.add_course

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.NumberPicker
import androidx.core.view.get
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.Uis
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewPagerAdapter
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_course.*
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 12:28
 */
class AddCourseFragment : BaseFragment() {
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var schedule: Schedule
    private lateinit var course: Course
    private var hasCourseData = false
    override fun layoutId(): Int = R.layout.fragment_add_course

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
            CommonDialogs.getEditDialog(activity(), if (hasCourseData) course.coureName else courseNameSubTitle.text.toString(), hasCourseData, okCall = {
                course.coureName = it
                courseNameSubTitle.text = it
            }).show()
        }

        editCoursePosition.setOnClickListener {
            CommonDialogs.getEditDialog(activity(), if (hasCourseData) "${course.teachingBuildingName}${course.classroomName}" else coursePositionSubtitle.text.toString(), hasCourseData, okCall = { t ->
                course.teachingBuildingName = t
                //如果是修改，直接保存building就可
                if (hasCourseData) {
                    course.classroomName = ""
                }
                courseScoreSubtitle.text = t
            }).show()
        }

        editCourseTeacher.setOnClickListener {
            CommonDialogs.getEditDialog(activity(), if (hasCourseData) course.teacherName else courseTeacherSubtitle.text.toString(), hasCourseData, okCall = { t ->
                course.teacherName = t
                courseTeacherSubtitle.text = t
            }).show()
        }

        editCourseScore.setOnClickListener {
            CommonDialogs.getEditDialog(activity(), if (hasCourseData) course.xf else courseScoreSubtitle.text.toString(), hasCourseData, okCall = { t ->
                course.xf = t
                courseScoreSubtitle.text = getString(R.string.score_2_0, t)
            }).show()
        }

        editCoursePlan.setOnClickListener { showPlanTime() }

        addCourse.setOnClickListener {
            if (course.coureName.isEmpty() || course.classWeek.isEmpty() || course.classDay.isEmpty()
                    || course.classSessions.isEmpty() || course.continuingSession.isEmpty()) {
                Toasts.toast(getString(R.string.param_empty))
            } else {
                courseViewModel.apply {
                    if (hasCourseData) update(course) else insert(course)
                }
                Toasts.toast(getString(R.string.handle_success))
                Navigations.close(this)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCourseUi() {
        courseNameSubTitle.text = course.coureName
        courseTimeSubtitle.text = "${course.getWeekDescription()}\n${course.classDayDescription} ${course.sessionDescription}"
        coursePositionSubtitle.text = "${course.teachingBuildingName}${course.classroomName}"
        courseTeacherSubtitle.text = course.teacherName
        courseScoreSubtitle.text = course.xf
        delete.visibility = View.VISIBLE;
        delete.setOnClickListener { deleteCourse() }
    }

    private fun deleteCourse() {
        CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_schedule_dialog_msg), okCall =  {
                courseViewModel.deleteByCourseId(course.id)
                Toasts.toast(getString(R.string.handle_success))
                Navigations.close(this@AddCourseFragment)
        }).show()
    }

    private fun showPlanTime() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            val bt = this
            dismissWithAnimation = true
            val view = Uis.inflate(activity(), R.layout.view_base_btm_dialog_ui)
            setContentView(view)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            view.animate().translationY(50F)
            view.findViewById<FrameLayout>(R.id.view_base_btm_dialog_ui).apply {
                addView(getWeekView(this, bt))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeekView(frameLayout: FrameLayout, bt: BottomSheetDialog): View? {
        val weekView = Uis.inflate(activity(), R.layout.view_week_choose)
        val chipGroup: ChipGroup = weekView.findViewById(R.id.chipGroup)
        chipGroup.removeAllViews()
        for (i in 0 until schedule.totalWeek) {
            Chip(activity()).apply {
                text = "${i + 1}"
                isCheckable = true
                isCheckedIconVisible = true
                if (hasCourseData && course.classWeek.isNotEmpty() && course.classWeek[i] == '1') {
                    isCheckable = true
                }
                chipGroup.addView(this)
            }
        }
        weekView.findViewById<Button>(R.id.single).setOnClickListener {
            chipGroup.clearCheck()
            for (i in 0 until schedule.totalWeek) {
                if (i % 2 == 0) (chipGroup[i] as Chip).isChecked = true
            }
        }
        weekView.findViewById<Button>(R.id.doubleWeek).setOnClickListener {
            chipGroup.clearCheck()
            (0 until schedule.totalWeek).forEach {
                if (it % 2 == 1) (chipGroup[it] as Chip).isChecked = true
            }
        }
        weekView.findViewById<Button>(R.id.all).setOnClickListener {
            chipGroup.clearCheck()
            (0 until schedule.totalWeek).forEach {
                (chipGroup[it] as Chip).isChecked = true
            }
        }
        weekView.findViewById<Button>(R.id.button).setOnClickListener {
            StringBuilder().apply {
                (0 until schedule.totalWeek).forEach {
                    if ((chipGroup[it] as Chip).isChecked) append(1) else append(0)
                }
                if (toString().contains("1")) {
                    course.classWeek = toString()
                    frameLayout.removeAllViews()
                    frameLayout.addView(getDayView(frameLayout, bt))
                }
            }
        }
        return weekView
    }

    private fun getDayView(frameLayout: FrameLayout, bt: BottomSheetDialog): View? {
        val dayView = Uis.inflate(activity(), R.layout.view_number_picker)
        val weeks = arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
        dayView.findViewById<NumberPicker>(R.id.number_picker).apply {
            displayedValues = weeks
            minValue = 0
            maxValue = weeks.size - 1
            if (hasCourseData && course.classDay.isNotEmpty()) {
                value = course.classDay.toInt() - 1
            }
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            dayView.findViewById<Button>(R.id.button).apply {
                course.classDay = "${value + 1}"
                frameLayout.removeAllViews()
                frameLayout.addView(getSessionView(frameLayout, bt))
            }
        }
        return dayView
    }

    @SuppressLint("SetTextI18n")
    private fun getSessionView(frameLayout: FrameLayout, bt: BottomSheetDialog): View? {
        val sessionView = Uis.inflate(activity(), R.layout.view_session_choose)
        val s = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val p1 = sessionView.findViewById<NumberPicker>(R.id.number_picker).apply {
            displayedValues = s
            minValue = 0
            maxValue = s.size - 1
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            if (hasCourseData && course.classSessions.isNotEmpty()) {
                value = course.classSessions.toInt() - 1
            }
        }
        val p2 = sessionView.findViewById<NumberPicker>(R.id.numberPicker2).apply {
            displayedValues = s
            minValue = 0
            maxValue = s.size - 1
            descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            if (hasCourseData && course.continuingSession.isNotEmpty()) {
                value = course.continuingSession.toInt() - 1
            }
        }
        sessionView.findViewById<Button>(R.id.button).apply {
            val r1 = s[p1.value]
            val r2 = s[p2.value]
            if (r1.toInt() + r2.toInt() - 1 <= schedule.maxSession) {
                course.classSessions = r1
                course.continuingSession = r2
                bt.dismiss()
                courseTimeSubtitle.text = "${course.getWeekDescription()}\n${course.classDayDescription} ${course.sessionDescription}"
            } else {
                Toasts.toast(getString(R.string.overlimit));
            }
        }
        return sessionView
    }

    override fun onBackPressed() {
        val condition = course.coureName.isNotEmpty() || course.classWeek.isNotEmpty() || course.classDay.isNotEmpty() || course.classSessions.isNotEmpty() || course.continuingSession.isNotEmpty()
        if (condition) {
            view?.let {
                Snackbar.make(it, getString(R.string.snack_course_edit), Snackbar.LENGTH_LONG).setAction(R.string.btn_ok) { Navigations.close(this) }.show()
            }
        } else {
            super.onBackPressed()
        }
    }
}
