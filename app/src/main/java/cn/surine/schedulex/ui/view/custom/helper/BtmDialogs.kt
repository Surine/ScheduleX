package cn.surine.schedulex.ui.view.custom.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.controller.ViewHolder
import cn.surine.schedulex.base.interfaces.Call
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.base.utils.Dates.*
import cn.surine.schedulex.base.utils.Navigations.open
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.ui.course.CourseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.peanut.sdk.miuidialog.MIUIDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 11:45
 */
object BtmDialogs {
    const val COURSE_ID = "course_id"
    const val COURSE_NAME = "course_name"

    /**
     * 获取一个底部弹窗的基础UI
     *
     * @return
     */
    fun getBaseConfig(context: Context, view: View, block: (view: View, bt: BottomSheetDialog) -> Unit) = BottomSheetDialog(context, R.style.BottomSheetDialogTheme).apply {
        setContentView(view)
        window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dismissWithAnimation = true
        springAnimation(view)
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
    fun showCourseInfoBtmDialog(baseFragment: @NotNull BaseFragment, mDatas: @NotNull MutableList<Course>, isThisWeek:MutableList<Boolean>, courseViewModel: CourseViewModel, call: Call) {
        val bt = BottomSheetDialog(baseFragment.activity(), R.style.BottomSheetDialogTheme)
        var view: View
        bt.setContentView(Uis.inflate(baseFragment.activity(), R.layout.view_recycle_list).also { view = it })
        bt.window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bt.dismissWithAnimation = true
        bt.show()

        springAnimation(view)
        val rec = view.findViewById<RecyclerView>(R.id.recyclerview)
        rec.load(LinearLayoutManager(baseFragment.activity()), CourseInfoAdapter(baseFragment.activity(), mDatas,isThisWeek, R.layout.item_course_info, BR.course)) {
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.courseEdit) {
                override fun onClick(v: View?, position: Int) {
                    open(baseFragment, R.id.action_scheduleFragment_to_addCourseFragment, Bundle().apply {
                        putString(COURSE_ID, mDatas[position].id)
                        putString(COURSE_NAME, mDatas[position].coureName)
                    })
                    bt.dismiss()
                }
            })
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.exam_plan) {
                override fun onClick(v: View?, position: Int) {
                    getBaseConfig(context = baseFragment.activity(), view = Uis.inflate(baseFragment.activity(), R.layout.view_exam_plan)) { view, _ ->
                        val course = mDatas[position]
                        val examName: EditText = view.findViewById(R.id.examName)
                        val examPos: EditText = view.findViewById(R.id.examPosition)
                        val examSetting: ConstraintLayout = view.findViewById(R.id.examItem)
                        val examButton: MaterialButton = view.findViewById(R.id.examButton)
                        val timers: LinearLayout = view.findViewById(R.id.timers)
                        val settingItemSubtitle: TextView = view.findViewById(R.id.settingItemSubtitle)
                        val slider: Slider = view.findViewById(R.id.sliderbar)
                        val picker: TimePicker = view.findViewById(R.id.timerpicker)
                        examName.setText(course.coureName)
                        examSetting.setOnClickListener {
                            timers.visibility = if (timers.visibility == GONE) VISIBLE else GONE
                        }
                        var sliderValue = 1
                        var timerPickerValue = getDate(HHmm)
                        slider.addOnChangeListener { _, value, _ ->
                            sliderValue = value.toInt()
                            settingItemSubtitle.text = "$sliderValue 天后 [${getDateFormat(getDateBeforeOfAfter(getDate(yyyyMMdd), sliderValue), yyyyMMdd)}] $timerPickerValue"
                        }
                        picker.setOnTimeChangedListener { _, hourOfDay, minute ->
                            timerPickerValue = "$hourOfDay:${minute.supplyZero()}"
                            settingItemSubtitle.text = "$sliderValue 天后 [${getDateFormat(getDateBeforeOfAfter(getDate(yyyyMMdd), sliderValue), yyyyMMdd)}] $timerPickerValue"
                        }
                        examButton.setOnClickListener {
                            RxPermissions(baseFragment.activity()).apply {
                                request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).subscribe {
                                    if (it) {
                                        val startTime = getDate("${getDateFormat(getDateBeforeOfAfter(getDate(yyyyMMdd), sliderValue), yyyyMMdd)} $timerPickerValue", yyyyMMddHHmm).time
                                        CoroutineScope(Dispatchers.IO).launch {
                                            Calendars.addCalendarEvent(baseFragment.activity(), "${examName.text}考试", examPos.text.toString()
                                                    , startTime, startTime + 2 * ONE_HOUR
                                            )
                                            withContext(Dispatchers.Main) {
                                                Toasts.toast("添加成功！")
                                            }
                                        }
                                    } else {
                                        Toasts.toast(baseFragment.activity().getString(R.string.permission_is_denied))
                                    }
                                }
                            }
                        }
                    }
                }
            })
            var modifyTag = false
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.memo) {
                override fun onClick(v: View?, position: Int) {
                    MIUIDialog(baseFragment.activity()).show {
                        title(text = "记录点什么吧~")
                        input(prefill = mDatas[position].memo, multiLines = true) { charSequence, _ ->
                            mDatas[position].memo = charSequence.toString()
                            courseViewModel.insert(mDatas[position])
                            modifyTag = true
                        }
                        positiveButton(text = "保存")
                        negativeButton(text = "取消")
                    }
                }
            })
            bt.setOnDismissListener {
                if (modifyTag) call.back()
            }
        }

    }

    private fun springAnimation(view: View) {
        val spring = SpringForce(0F)
                .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_LOW)

        val anim = SpringAnimation(view, DynamicAnimation.X, 0F)
                .setSpring(spring)
                .setStartVelocity(3000F)
        anim.start()
    }


    /**
     * 自定义详情适配器
     * */
    class CourseInfoAdapter(val context: Context, private val list: List<Course>, private val isThisWeek: MutableList<Boolean>, layoutId: Int, bindName: Int) : BaseAdapter<Course>(list, layoutId, bindName) {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val course = list[position]
            val tvCourseName = holder.itemView.findViewById<TextView>(R.id.courseName)
            if(!isThisWeek[position]){
                tvCourseName.text = "${tvCourseName.text}【非本周】"
            }
            tvCourseName.text = tvCourseName.text
            holder.itemView.findViewById<TextView>(R.id.courseClassDay).background = Drawables.getDrawable(Color.parseColor(course.color), 180, 0, 0)
            holder.itemView.findViewById<TextView>(R.id.courseSession).background = Drawables.getDrawable(App.context.resources.getColor(R.color.colorPrimary), 180, 0, 0)
            val mainContainer = holder.itemView.findViewById<View>(R.id.courseInfoMainContent)
            if(isThisWeek[position]){
                //本周
                mainContainer.setBackgroundResource(R.drawable.shape_rect_10_white)
            }else{
                mainContainer.setBackgroundResource(R.drawable.shape_rect_10_transparentwhite)
            }
        }
    }


}