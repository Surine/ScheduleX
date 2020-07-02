package cn.surine.schedulex.ui.view.custom.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.Dates
import cn.surine.schedulex.base.utils.Drawables
import cn.surine.schedulex.base.utils.Navigations.open
import cn.surine.schedulex.base.utils.Uis
import cn.surine.schedulex.data.entity.Course
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:45
 */
object BtmDialogs {
    const val COURSE_ID = "course_id"

    /**
     * 获取一个底部弹窗的基础UI
     *
     * @return
     */
    fun getBaseConfig(context: Context, view: View, block: (view: View, bt: BottomSheetDialog) -> Unit) = BottomSheetDialog(context, R.style.BottomSheetDialogTheme).apply {
        setContentView(view)
        window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dismissWithAnimation = true
        view.animate().translationY(50f)
        block(view, this)
        show()
    }


    /**
     * 显示课详情
     *
     * @param baseFragment       上下文
     * @param course             课程
     * @param alphaForCourseItem
     */
    @SuppressLint("SetTextI18n")
    fun showCourseInfoBtmDialog(baseFragment: BaseFragment, course: Course, alphaForCourseItem: Int) {
        val bt = BottomSheetDialog(baseFragment.activity(), R.style.BottomSheetDialogTheme)
        var view: View
        bt.setContentView(Uis.inflate(baseFragment.activity(), R.layout.view_course_info).also { view = it })
        bt.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bt.dismissWithAnimation = true
        bt.show()
        view.animate().translationY(50f)
        val courseName = view.findViewById<TextView>(R.id.courseName)
        val coursePosition = view.findViewById<TextView>(R.id.coursePosition)
        val courseClassDay = view.findViewById<TextView>(R.id.courseClassDay)
        val courseSession = view.findViewById<TextView>(R.id.courseSession)
        val courseTeacher = view.findViewById<TextView>(R.id.courseTeacher)
        val courseScore = view.findViewById<TextView>(R.id.courseScore)
        val courseWeekInfo = view.findViewById<TextView>(R.id.weekInfo)
        val edit = view.findViewById<ImageView>(R.id.courseEdit)
        courseName.text = course.coureName
        var positionText: String?
        coursePosition.text = if (TextUtils.isEmpty((course.teachingBuildingName + course.classroomName).also { positionText = it })) "无位置" else positionText
        courseClassDay.text = "周" + Dates.getWeekInChi(course.classDay.toInt())
        courseSession.text = course.classSessions + "-" + (course.continuingSession.toInt() + course.classSessions.toInt() - 1) + "节"
        courseTeacher.text = if (TextUtils.isEmpty(course.teacherName)) App.context.resources.getString(R.string.unknown) else course.teacherName
        courseScore.text = if (TextUtils.isEmpty(course.xf)) App.context.resources.getString(R.string.unknown) else course.xf + "分"
        courseWeekInfo.text = course.getWeekDescription()
        if (alphaForCourseItem > 0) {
            courseSession.background = Drawables.getDrawable(Color.parseColor(course.color), 180, 0, 0)
        } else {
            courseSession.background = Drawables.getDrawable(Color.TRANSPARENT, 180, 4, Color.BLACK)
            courseSession.setTextColor(Color.BLACK)
        }
        courseClassDay.background = Drawables.getDrawable(App.context.resources.getColor(R.color.colorPrimary), 180, 0, 0)
        val bundle = Bundle()
        bundle.putString(COURSE_ID, course.id)
        edit.setOnClickListener {
            open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, bundle)
            bt.dismiss()
        }
        view.findViewById<Button>(R.id.button3).setOnClickListener {
            getBaseConfig(context = baseFragment.activity(),view = Uis.inflate(baseFragment.activity(),R.layout.view_exam_plan)){
                view, bt ->
            }
        }
    }


}