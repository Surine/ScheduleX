package cn.surine.schedulex.ui.course.op_delegate

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.hide
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.base.utils.show
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.ui.course.AddCourseFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_course_v2.*
import kotlinx.android.synthetic.main.item_add_course_block.*
import kotlinx.android.synthetic.main.view_course_info.*
import kotlin.random.Random

class CreateCourseDelegate:CourseOpDelegate{
    override fun initDelegate(fragment: AddCourseFragment) {
        //初始化课程
        fragment.mCourse = Course().apply {
            scheduleId = fragment.schedule.roomId.toLong()
            color = Constants.COLOR_1[Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.size)]
            id = buildId(scheduleId)
        }

        //修改课程名字
        fragment.editCourseName.setOnClickListener {
            MIUIDialog(fragment.activity()).show {
                title(text = "编辑课程名称")
                input(hint = "请输入课程名称") { it, _ ->
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
                    fragment.mCourse.color = "#${color.toString(16)}"
                    fragment.editCourseColorSubtitle.text = "已选择:${fragment.mCourse.color}"
                }
                positiveButton(text = "确定")
            }
        }

        fragment.mCoursePlanBlockData.clear()
        fragment.mCoursePlanBlockData.add(CoursePlanBlock())

        //添加新的时间段
        fragment.addNewPlan.setOnClickListener {
            if(fragment.mCoursePlanBlockData.size >= 5){
                Toasts.toast("最多只能添加5个时间段")
            }else{
                fragment.mCoursePlanBlockData.add(CoursePlanBlock())
                fragment.coursePlanRecycler.adapter?.notifyItemInserted(fragment.mCoursePlanBlockData.size)
            }
        }


        //初始化列表
        fragment.coursePlanRecycler.load(mLayoutManager = LinearLayoutManager(fragment.activity,LinearLayoutManager.VERTICAL,false),
                    mAdapter = BaseAdapter(fragment.mCoursePlanBlockData, R.layout.item_add_course_block,BR.coursePlanBlock)
                ){
            //删除事件
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.coursePlanItemDelete) {
                override fun onClick(v: View?, position: Int) {
                    if(fragment.mCoursePlanBlockData.size == 1){
                        Toasts.toast("至少保留一个时间段")
                    }else{
                        fragment.mCoursePlanBlockData.removeAt(position)
                        it.notifyItemRemoved(position)
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
                    MIUIDialog(fragment.activity()).show {
                        customView(R.layout.view_select_weeks){

                        }
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
                                Toasts.toast("选择了:$week")
                                fragment.mCoursePlanBlockData[position].day = week
                                fragment.coursePlanRecycler.adapter?.notifyItemChanged(position)
                                val dialog = this
                                v.postDelayed({
                                    dialog.dismiss()
                                },1000)
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
                            rangeSlider?.values = mutableListOf(1F,rangeSlider?.valueTo ?: 2 /2F)
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
}