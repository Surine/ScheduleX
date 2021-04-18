package cn.surine.schedulex.ui.course

import android.view.View
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.op_delegate.CopyCourseDelegate
import cn.surine.schedulex.ui.course.op_delegate.CourseOpDelegate
import cn.surine.schedulex.ui.course.op_delegate.CreateCourseDelegate
import cn.surine.schedulex.ui.course.op_delegate.ModifyCourseDelegate
import cn.surine.schedulex.ui.schedule.ScheduleViewModel
import cn.surine.schedulex.ui.schedule.ScheduleViewPagerAdapter
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs

class AddCourseFragment :BaseFragment(){
    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var courseViewModel: CourseViewModel
    lateinit var schedule: Schedule
    lateinit var mCourse: Course
    var hasCourseData = false

    //课程安排块数据
    var mCoursePlanBlockData = mutableListOf<CoursePlanBlock>()
    //当前操作
    lateinit var curOpDelegate:CourseOpDelegate

    override fun layoutId() = R.layout.fragment_add_course_v2

    override fun onInit(parent: View?) {
        //vm
        VmManager(this).apply {
            scheduleViewModel = vmSchedule
            courseViewModel = vmCourse
        }
        //当前课表
        schedule = scheduleViewModel.curSchedule

        //选择合适的委托
        curOpDelegate = when{
            requireArguments().getBoolean(ScheduleViewPagerAdapter.IS_COPY) -> {
                CopyCourseDelegate()
            }
            requireArguments().getString(BtmDialogs.COURSE_ID,"").isNotEmpty() -> {
                ModifyCourseDelegate()
            }
            else -> CreateCourseDelegate()
        }

        //开始加载
        curOpDelegate.initDelegate(this)
    }

}