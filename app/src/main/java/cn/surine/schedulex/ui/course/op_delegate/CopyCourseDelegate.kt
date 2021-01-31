package cn.surine.schedulex.ui.course.op_delegate

import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.ui.course.AddCourseFragment
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs

class CopyCourseDelegate :CreateCourseDelegate(){
    override fun initCourseData(fragment: AddCourseFragment) {
        super.initCourseData(fragment)
        val otherData = fragment.courseViewModel.getCourseById(fragment.requireArguments().getString(BtmDialogs.COURSE_ID))
        fragment.mCourse.coureName = otherData.coureName
        fragment.mCourse.id = buildId(otherData.scheduleId)
        val list = mutableListOf<Int>()
        for (i in otherData.classWeek.indices) {
            if (otherData.classWeek[i] == '1') {
                list.add(i + 1)
            }
        }
        fragment.mCoursePlanBlockData.add(CoursePlanBlock(
                belongId = fragment.mCourse.id,
                weeks = list,
                day = otherData.classDay.toInt(),
                sessionStart = otherData.classSessions.toInt(),
                sessionEnd = otherData.classSessions.toInt() + otherData.continuingSession.toInt() - 1,
                teacher = otherData.teacherName,
                position = otherData.campusName + otherData.teachingBuildingName + otherData.classroomName,
                score = otherData.xf
        ))
    }
}