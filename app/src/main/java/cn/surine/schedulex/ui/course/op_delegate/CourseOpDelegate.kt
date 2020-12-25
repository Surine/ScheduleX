package cn.surine.schedulex.ui.course.op_delegate

import cn.surine.schedulex.ui.course.AddCourseFragment

interface CourseOpDelegate {

    /**
     * 生成ID
     * */
    fun buildId(scheduleId: Long) = "${scheduleId}@${System.currentTimeMillis()}"

    /**
     * 初始化委托
     * */
    fun initDelegate(fragment: AddCourseFragment)
}