package cn.surine.schedulex.ui.add_timetable

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.*
import cn.surine.schedulex.data.entity.TimeTable
import cn.surine.schedulex.data.entity.TimeTableDisplayEntity
import cn.surine.schedulex.ui.timetable_list.TimeTableListFragment
import cn.surine.schedulex.ui.timetable_list.TimeTableViewModel
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_timetable.*
import kotlinx.android.synthetic.main.fragment_schedule_school_list.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 22:05
 */
class AddTimeTableFragment : BaseFragment() {
    private lateinit var timetableViewModel: TimeTableViewModel
    lateinit var timetable: TimeTable
    override fun layoutId(): Int = R.layout.fragment_add_timetable
    val data = mutableListOf<TimeTableDisplayEntity>()
    var isAdd = false
    var mSessionTime = 45
    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun onInit(parent: View?) {
        VmManager(this).apply {
            timetableViewModel = vmTimetable
        }
        if (arguments != null) {
            timetable = timetableViewModel.getTimTableById(requireArguments().getInt(TimeTableListFragment.TIMETABLE_ID, -1).toLong())
                    ?: return
            isAdd = false
        } else {
            timetable = TimeTable.tedaNormal()
            timetable.name = ""
            isAdd = true
        }
        editText.setText(timetable.name)
        mSessionTime = timetable.rule.split(",")[0].toInt()
        tvSessionText.text = "$mSessionTime 分钟"
        settingItemEverySessionTime.setOnClickListener { showSessionTimeDialog() }
        dataMappingTT2TD(timetable)
        timetableList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                addTimeTable.apply { if (dy > 0) shrink() else extend() }
            }
        })
        timetableList.load(LinearLayoutManager(activity()), BaseAdapter<TimeTableDisplayEntity>(data, R.layout.item_time_table_session_info, BR.timeDisplay)) {
            it.setOnItemClickListener { position ->
                CommonDialogs.timePickerDialog(activity(), getString(R.string.please_choose_time), data[position].startTime!!.split(":")[0].toInt(), data[position].startTime!!.split(":")[1].toInt()) { _, hourOfDay, minute ->
                    //如果选择的时间不合理就提示
                    if (position > 0 && (hourOfDay * 60 + minute) < Dates.getTransformTimeString(data[position - 1].endTime)) {
                        Snackbar.make(addTimeTable, R.string.time_rule, Snackbar.LENGTH_SHORT).show()
                    } else {
                        changeStartTimeUpdate(position, (Dates.getTransformTimeString(data[position].startTime) - (hourOfDay * 60 + minute)).toInt())
                    }
                }.show()
            }

            it.setOnItemLongClickListener { position ->
                if (position < 6) {
                    Toasts.toast(getString(R.string.delete_rule))
                } else {
                    CommonDialogs.getCommonDialog(activity(), getString(R.string.warning), getString(R.string.delete_timetable_session_note), okCall = {
                        if (data.size > position) {
                            data.subList(position, data.size).clear()
                        }
                        it.notifyDataSetChanged()
                        Toasts.toast(getString(R.string.delete))
                    })
                }
                true
            }
        }
        addSession.setOnClickListener {
            val entity = data[data.size - 1]
            val lastEndTime = Dates.getTransformTimeString(entity.endTime)
            if (entity.session >= Constants.MAX_SESSION) {
                Toasts.toast(getString(R.string.max_support, Constants.MAX_SESSION))
                return@setOnClickListener
            }
            if (lastEndTime + 10 > 24 * 60 || lastEndTime + 10 + mSessionTime > 24 * 60) {
                Toasts.toast(getString(R.string.need_today))
                return@setOnClickListener
            }
            data.add(TimeTableDisplayEntity(entity.session + 1, Dates.getTransformTimeNumber(lastEndTime + 10), Dates.getTransformTimeNumber(lastEndTime + 10 + mSessionTime)))
            timetableList.adapter!!.notifyItemInserted(data.size)
            timetableList.smoothScrollToPosition(data.size)
        }
        addTimeTable.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                Toasts.toast(getString(R.string.param_empty))
                return@setOnClickListener
            }
            timetable.name = editText.text.toString()
            val checkStatus = dataMappingTD2TT(data)
            if (!checkStatus) {
                Toasts.toast(getString(R.string.need_today))
                return@setOnClickListener
            }
            timetableViewModel.apply { if (isAdd) addTimeTable(timetable) else updateTimeTable(timetable) }
            Toasts.toast(getString(R.string.handle_success))
            Navigations.close(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSessionTimeDialog() {
        BottomSheetDialog(activity(), R.style.BottomSheetDialogTheme).apply {
            dismissWithAnimation = true
            val baseView = Uis.inflate(activity(), R.layout.view_base_btm_dialog_ui)
            setContentView(baseView)
            window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
            val frameLayout = baseView.findViewById<FrameLayout>(R.id.view_base_btm_dialog_ui)
            val view = Uis.inflate(activity(), R.layout.view_number_picker)
            baseView.animate().translationY(20F)
            view.animate().translationY(50F)
            frameLayout.addView(view)
            view.findViewById<TextView>(R.id.dialog_title).setText(R.string.setting_one_session_time)
            val times = Array(23) { "${it * 5 + 10}" }
            val numberPicker = view.findViewById<NumberPicker>(R.id.number_picker).apply {
                displayedValues = times
                minValue = 0
                maxValue = times.size - 1
                descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
            }
            view.findViewById<Button>(R.id.button).setOnClickListener {
                mSessionTime = Integer.parseInt(times[numberPicker.value])
                this@AddTimeTableFragment.tvSessionText.text = "${mSessionTime}分钟"
                changeSessionTime(0)
                dismiss()
            }
        }
    }

    private fun changeStartTimeUpdate(position: Int, mTimeOffset: Int) {
        (position until data.size).forEach {
            //起始时间
            data[it].startTime = Dates.getTransformTimeNumber(Dates.getTransformTimeString(data[it].startTime) - mTimeOffset)
            //结束时间
            val curStartTime = Dates.getTransformTimeString(data[it].startTime)
            data[it].endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime)
            timetableList.adapter!!.notifyItemChanged(it)
        }
    }

    private fun changeSessionTime(position: Int) {
        //先记下旧的第一节下课时间
        var classOverTime = Dates.getTransformTimeString(data[0].endTime)
        //然后更新一下第一节的终止时间，然后继续更新后面的内容
        val curStartTime = Dates.getTransformTimeString(data[0].startTime)
        data[0].endTime = Dates.getTransformTimeNumber(curStartTime + mSessionTime)
        timetableList.adapter!!.notifyItemChanged(0)
        ((position + 1) until data.size).forEach {
            //从第2个开始
            //计算起始时间 = 新的上一节终止时间 + 课间时间（旧的本节开始时间 - 旧的上节终止时间）
            data[it].startTime = Dates.getTransformTimeNumber(Dates.getTransformTimeString(data[it - 1].endTime) + Dates.getTransformTimeString(data[it].startTime) - classOverTime)
            //重新赋值(旧的本节终止时间，留给下一轮循环使用)
            classOverTime = Dates.getTransformTimeString(data[it].endTime)
            //新的本节结束时间
            val curStartTime2 = Dates.getTransformTimeString(data[it].startTime)
            data[it].endTime = Dates.getTransformTimeNumber(curStartTime2 + mSessionTime)
            timetableList.adapter!!.notifyItemChanged(it)
        }
    }

    private fun dataMappingTT2TD(timeTable: TimeTable) {
        val ruleData = timeTable.rule.split(",")
        var time = timeTable.startTime
        var i = 0
        var j = 1
        while (i < ruleData.size - 1) {
            val timeTableDisplayEntity = TimeTableDisplayEntity()
            timeTableDisplayEntity.session = j
            timeTableDisplayEntity.startTime = Dates.getTransformTimeNumber(time)
            time += ruleData[i].toInt()
            timeTableDisplayEntity.endTime = Dates.getTransformTimeNumber(time)
            data.add(timeTableDisplayEntity)
            time += Integer.parseInt(ruleData[i + 1])
            j++
            i += 2
        }
    }

    private fun dataMappingTD2TT(timeTableDisplayEntities: List<TimeTableDisplayEntity>): Boolean {
        StringBuilder().apply {
            (timeTableDisplayEntities.indices).forEach {
                if (it == 0) {
                    timetable.startTime = Dates.getTransformTimeString(timeTableDisplayEntities[it].startTime)
                }
                //一节课
                append(Dates.getTransformTimeString(timeTableDisplayEntities[it].endTime) - Dates.getTransformTimeString(timeTableDisplayEntities[it].startTime))
                append(",")
                //课间
                if (it != timeTableDisplayEntities.size - 1) {
                    append(Dates.getTransformTimeString(timeTableDisplayEntities[it + 1].startTime) - Dates.getTransformTimeString(timeTableDisplayEntities[it].endTime))
                    append(",")
                }
            }
            //防止越界安全
            append("0")
            //不允许包含负数，一般是咵天导致的
            return if (toString().contains("-")) false else {
                timetable.rule = toString()
                true
            }
        }
    }
}
