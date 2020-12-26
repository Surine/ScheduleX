package cn.surine.schedulex.ui.course.op_delegate

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.ui.course.AddCourseFragment
import cn.surine.schedulex.ui.view.custom.helper.SelectWeeksDialog
import cn.surine.ui_lib.setting
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_course_v2.*
import kotlinx.android.synthetic.main.view_confirm_course_block.view.*
import kotlinx.android.synthetic.main.view_dsl_setting.*
import okhttp3.internal.toHexString
import kotlin.random.Random

open class CreateCourseDelegate:CourseOpDelegate{
    var deleteConfirm = false

    override fun initDelegate(fragment: AddCourseFragment) {
        //初始化课程
        initCourseData(fragment)

        //设置
        deleteConfirm = Prefs.getBoolean("course_block_delete",false)
        fragment.help.setOnClickListener {
            MaterialDialog(fragment.activity(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                customView(R.layout.view_dsl_setting)
                fragment.setting(rootView) {
                    group {
                        switchItem("时间段列表布局", openSubTitle = "横向布局", closeSubTitle = "纵向布局", initValue = false, tag = "course_block_layout") { _, isChecked ->
                            if (isChecked) {
                                fragment.coursePlanRecycler.layoutManager = LinearLayoutManager(fragment.activity, LinearLayoutManager.HORIZONTAL, false)
                            } else {
                                fragment.coursePlanRecycler.layoutManager = LinearLayoutManager(fragment.activity, LinearLayoutManager.VERTICAL, false)
                            }
                        }
                        switchItem("删除时间段前确认", openSubTitle = "开启确认", closeSubTitle = "关闭确认", initValue = false, tag = "course_block_delete") { _, isChecked ->
                           deleteConfirm = isChecked
                        }
                    }
                }
            }
        }


        fragment.confirmFab.setOnClickListener {
            MIUIDialog(fragment.activity()).show {
                customView(viewRes = R.layout.view_confirm_course_block) {
                    it.confirmCourseName.text = fragment.mCourse.coureName
                    it.confirmRecyclerView.load(mLayoutManager = LinearLayoutManager(fragment.activity),
                        mAdapter = BaseAdapter(fragment.mCoursePlanBlockData,R.layout.item_confirm_course,BR.confirmData))
                }
                positiveButton(text = "确认无误") {
                    saveCourseInfo(fragment)
                }
            }
        }


        //修改课程名字
        fragment.editCourseName.setOnClickListener {
            MIUIDialog(fragment.activity()).show {
                title(text = "编辑课程名称")
                input(prefill = if(fragment.mCourse.coureName.isNotEmpty()) fragment.mCourse.coureName
                        else null,hint = "请输入课程名称") { it, _ ->
                    fragment.mCourse.coureName = it.toString()
                    fragment.courseNameSubTitle.text = it
                }
                positiveButton(text = "保存") { }
                negativeButton(text = "取消") { }
            }
        }

        //修改课程颜色
        fragment.editCourseColor.setOnClickListener {
            MaterialDialog(fragment.activity(),BottomSheet()).show {
                title(text = "选择课程颜色")
                colorChooser(colors = ColorPalette.Primary,
                    subColors = ColorPalette.PrimarySub,
                    allowCustomArgb = true){
                    _,color ->
                    fragment.mCourse.color = "#${color.toHexString()}"
                    fragment.editCourseColorSubtitle.text = "已选择:${fragment.mCourse.color}"
                    fragment.s2Img.setBackgroundColor(color)
                }
                positiveButton(text = "确定")
            }
        }


        if(fragment.mCoursePlanBlockData.size == 0){
            fragment.mCoursePlanBlockData.add(CoursePlanBlock())
        }

        //添加新的时间段
        fragment.addNewPlan.setOnClickListener {
            if(fragment.mCoursePlanBlockData.size >= 5){
                Toasts.toast("最多只能添加5个时间段")
            }else{
                fragment.mCoursePlanBlockData.add(0,CoursePlanBlock())
                fragment.coursePlanRecycler.adapter?.notifyItemInserted(0)
                Toasts.toast("添加成功")
            }
        }


        //初始化列表
        var linearLayoutManager:LinearLayoutManager = if (!Prefs.getBoolean("course_block_layout",false)){
            LinearLayoutManager(fragment.activity,LinearLayoutManager.VERTICAL,false)
        }else{
            LinearLayoutManager(fragment.activity,LinearLayoutManager.HORIZONTAL,false)
        }
        fragment.coursePlanRecycler.load(mLayoutManager = linearLayoutManager,
                    mAdapter = BaseAdapter(fragment.mCoursePlanBlockData, R.layout.item_add_course_block,BR.coursePlanBlock)
                ){
            //删除事件
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.coursePlanItemDelete) {
                override fun onClick(v: View?, position: Int) {
                    if(fragment.mCoursePlanBlockData.size == 1){
                        Toasts.toast("至少保留一个时间段")
                    }else{
                        if(deleteConfirm){
                            MIUIDialog(fragment.activity()).show {
                                title(text = "确认删除")
                                message(text = "确认删除该时间段吗？此操作不可恢复，请谨慎")
                                positiveButton(text = "确定"){ m->
                                    fragment.mCoursePlanBlockData.removeAt(position)
                                    fragment.coursePlanRecycler.adapter?.notifyItemRemoved(position)
                                }
                                negativeButton(text = "取消") {  }
                            }
                        }else{
                            fragment.mCoursePlanBlockData.removeAt(position)
                            fragment.coursePlanRecycler.adapter?.notifyItemRemoved(position)
                        }
                    }
                }
            })
            //展开
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.coursePlanItemExpand){
                override fun onClick(v: View?, position: Int) {
                    fragment.mCoursePlanBlockData[position].expand = !fragment.mCoursePlanBlockData[position].expand
                    fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                }
            })
            //编辑周信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseWeek){
                override fun onClick(v: View?, position: Int) {
                    SelectWeeksDialog().show(fragment.activity(),fragment.schedule.totalWeek,fragment.mCoursePlanBlockData[position].weeks){ v->
                        Toasts.toast("已选择：$v")
                        fragment.mCoursePlanBlockData[position].weeks = v
                        fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                    }
                }
            })
            //编辑星期信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseDay){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).show {
                        customView(R.layout.view_select_day){v->
                            val chipGroup = v.findViewById<ChipGroup>(R.id.chipGroup)
                            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                                val week = when(checkedId){
                                    R.id.week_1 -> 1
                                    R.id.week_2 -> 2
                                    R.id.week_3 -> 3
                                    R.id.week_4 -> 4
                                    R.id.week_5 -> 5
                                    R.id.week_6 -> 6
                                    R.id.week_7 -> 7
                                    else -> 1
                                }
                                fragment.mCoursePlanBlockData[position].day = week
                                fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                                val dialog = this
                                v.postDelayed({
                                    dialog.dismiss()
                                },700)
                            }
                        }
                    }
                }
            })
            //编辑节次信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseSession){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).show {
                        var rangeSlider:RangeSlider? = null
                        customView(R.layout.view_select_session){ v->
                            rangeSlider = v.findViewById<RangeSlider>(R.id.rangeSlider)
                            rangeSlider?.valueFrom = 1F
                            rangeSlider?.valueTo = fragment.schedule.maxSession.toFloat()
                            rangeSlider?.values = mutableListOf(fragment.mCoursePlanBlockData[position].sessionStart.toFloat(),fragment.mCoursePlanBlockData[position].sessionEnd.toFloat())
                        }
                        positiveButton(text = "确定"){
                            fragment.mCoursePlanBlockData[position].sessionStart = (rangeSlider?.values?.get(0)
                                    ?: 1F).toInt()
                            fragment.mCoursePlanBlockData[position].sessionEnd = (rangeSlider?.values?.get(1) ?: 1F).toInt()
                            fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                        }
                        negativeButton(text = "取消")
                    }
                }
            })

            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCoursePosition){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).show {
                        title(text = "输入课程地点")
                        input(hint = "请输入课程地点") { it, _ ->
                            fragment.mCoursePlanBlockData[position].position = it.toString()
                            fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                        }
                        positiveButton(text = "保存") { }
                        negativeButton(text = "取消") { }
                    }
                }
            })
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseTeacher){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).show {
                        title(text = "编辑课程教师")
                        input(hint = "请输入课程教师") { it, _ ->
                            fragment.mCoursePlanBlockData[position].teacher = it.toString()
                            fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                        }
                        positiveButton(text = "保存") { }
                        negativeButton(text = "取消") { }
                    }
                }
            })
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseScore){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).show {
                        title(text = "编辑课程学分")
                        input(hint = "请输入课程学分") { it, _ ->
                            fragment.mCoursePlanBlockData[position].score = it.toString()
                            fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                        }
                        positiveButton(text = "保存") { }
                        negativeButton(text = "取消") { }
                    }
                }
            })
        }
    }

    private fun saveCourseInfo(fragment: AddCourseFragment) {
        for (index in fragment.mCoursePlanBlockData.indices){
            val planBlock = fragment.mCoursePlanBlockData[index]
            val course = Course().apply {
                id = buildId(fragment.schedule.roomId.toLong())
                coureName = fragment.mCourse.coureName
                color = fragment.mCourse.color
                classWeek = planBlock.weeks.bitCount()
                classDay = planBlock.day.toString()
                classSessions = planBlock.sessionStart.toString()
                continuingSession = ((planBlock.sessionEnd - planBlock.sessionStart + 1).toString())
                teacherName = planBlock.teacher
                teachingBuildingName = planBlock.position
                xf = planBlock.score
            }
            fragment.courseViewModel.insert(course)
        }
        Toasts.toast("添加成功！")
        Navigations.close(fragment)
    }

    open fun initCourseData(fragment: AddCourseFragment) {
        fragment.mCourse = Course().apply {
            scheduleId = fragment.schedule.roomId.toLong()
            color = Constants.COLOR_1[Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.size)]
            id = buildId(scheduleId)
        }
    }
}