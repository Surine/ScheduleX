package cn.surine.schedulex.ui.course.op_delegate

import cn.surine.schedulex.base.utils.Navigations
import cn.surine.schedulex.base.utils.Prefs
import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.ui.course.AddCourseFragment
import cn.surine.schedulex.ui.view.custom.helper.BtmDialogs
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_course_v2.*

class ModifyCourseDelegate : CreateCourseDelegate() {
    override fun initCourseData(fragment: AddCourseFragment) {
        if (fragment.arguments == null) return
        fragment.mCourse = fragment.courseViewModel.getCourseById(fragment.requireArguments().getString(BtmDialogs.COURSE_ID))
        val mCourseDatas = fragment.courseViewModel.getCourseByName(fragment.requireArguments().getString(BtmDialogs.COURSE_NAME))
        for (course in mCourseDatas) {
            val list = mutableListOf<Int>()
            for (i in course.classWeek.indices) {
                if (course.classWeek[i] == '1') {
                    list.add(i + 1)
                }
            }
            fragment.mCoursePlanBlockData.add(CoursePlanBlock(
                    belongId = course.id,
                    weeks = list,
                    day = course.classDay.toInt(),
                    sessionStart = course.classSessions.toInt(),
                    sessionEnd = course.classSessions.toInt() + course.continuingSession.toInt() - 1,
                    teacher = course.teacherName,
                    position = course.campusName + course.teachingBuildingName + course.classroomName,
                    score = course.xf
            ))
        }
        fragment.addCoursePageTitle.text = "编辑课程"
        fragment.confirmFab.text = "完成修改"
        //删除确认
        Prefs.save("course_block_delete", true)
    }

    //修改删除
    override fun deletePlanBlock(fragment: AddCourseFragment, position: Int) {
        if (deleteConfirm) {
            MIUIDialog(fragment.activity()).show {
                title(text = "确认删除")
                message(text = "确认删除该时间段吗？此操作直接影响现有数据，请务必谨慎操作～")
                positiveButton(text = "确定") {
                    deleteFunc(fragment, position)
                }
                negativeButton(text = "取消")
            }
        } else {
            deleteFunc(fragment, position)
        }
    }

    private fun deleteFunc(fragment: AddCourseFragment, position: Int) {
        val belongsId = mAdapter.mDatas[position].belongId
        mAdapter.removeData(position)
        fragment.courseViewModel.deleteByCourseId(belongsId)
        if (mAdapter.mDatas.size == 0) {
            Navigations.close(fragment)
        }
    }
}