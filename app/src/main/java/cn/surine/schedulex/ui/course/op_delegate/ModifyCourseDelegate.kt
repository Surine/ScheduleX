package cn.surine.schedulex.ui.course.op_delegate

import cn.surine.schedulex.ui.course.AddCourseFragment
import cn.surine.schedulex.ui.course.op_delegate.CourseOpDelegate
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs

class ModifyCourseDelegate : CreateCourseDelegate() {
    override fun initCourseData(fragment: AddCourseFragment) {
        if(fragment.arguments == null) return
        //TODO: 改成用课程名称查询
        fragment.mCourse = fragment.courseViewModel.getCourseById(fragment.requireArguments().getString(BtmDialogs.COURSE_ID))

    }
}