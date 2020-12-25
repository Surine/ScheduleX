package cn.surine.schedulex.ui.course.op_delegate

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.CoursePlanBlock
import cn.surine.schedulex.ui.course.AddCourseFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_course_v2.*
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
            MaterialDialog(fragment.activity()).show {
                title(text = "选择课程颜色")
                colorChooser(colors = intArrayOf(Color.RED,Color.GREEN,Color.BLUE)){
                    _,color ->
                    fragment.mCourse.color = "#${color.toString(16)}"
                    fragment.editCourseColorSubtitle.text = "已选择:${fragment.mCourse.color}"
                }
                positiveButton(text = "确定")
            }
        }

        //添加新的时间段
        fragment.addNewPlan.setOnClickListener {
            if(fragment.mCoursePlanBlockData.size >= 5){
                Toasts.toast("最多只能添加5个时间段")
            }else{
                fragment.mCoursePlanBlockData.add(0, CoursePlanBlock())
                fragment.coursePlanRecycler.adapter?.notifyItemInserted(0)
            }
        }


        //初始化列表
        fragment.coursePlanRecycler.load(mLayoutManager = LinearLayoutManager(fragment.activity),
                    mAdapter = BaseAdapter(fragment.mCoursePlanBlockData, R.layout.item_add_course_block,BR.coursePlanBlock)
                ){
            //删除事件
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.coursePlanItemDelete) {
                override fun onClick(v: View?, position: Int) {
                    if(position == 0){
                        Toasts.toast("至少保留一个时间段")
                    }else{
                        fragment.mCoursePlanBlockData.removeAt(position)
                        it.notifyItemRemoved(position)
                    }
                }
            })
            //编辑周信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseWeek){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).customView(R.layout.view_select_weeks){

                    }
                }
            })
            //编辑星期信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseDay){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).customView(R.layout.view_select_day){v->
                       val chipGroup = v.findViewById<ChipGroup>(R.id.chipGroup)
                        chipGroup.setOnCheckedChangeListener { group, checkedId ->
                           Toasts.toast("$checkedId")
                        }
                    }
                }
            })
            //编辑节次信息
            it.setOnItemElementClickListener(object :BaseAdapter.OnItemElementClickListener(R.id.editCourseSession){
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(fragment.activity()).customView(R.layout.view_select_session){ v->
                        val rangeSlider = v.findViewById<RangeSlider>(R.id.rangeSlider)
                    }
                }
            })
        }
    }
}