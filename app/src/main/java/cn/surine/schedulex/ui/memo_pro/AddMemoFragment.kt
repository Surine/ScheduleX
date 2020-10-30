package cn.surine.schedulex.ui.memo_pro

import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.ktx.close
import cn.surine.schedulex.base.ktx.toast
import cn.surine.schedulex.base.utils.Dates
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Event
import cn.surine.schedulex.data.entity.Memo
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_add_memo.*
import kotlinx.android.synthetic.main.view_datepicker.view.*
import kotlinx.android.synthetic.main.view_exam_plan.view.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/28/20 16:41
 */
class AddMemoFragment : BaseFragment() {
    companion object {
        val keyMaps = mapOf(
                "_normal" to 0,
                "_exam" to 1,
                "_trip" to 2,
                "_meeting" to 3,
                "_other" to 4
        )
    }

    override fun layoutId() = R.layout.fragment_add_memo
    private lateinit var memoViewModel: MemoViewModel
    private val mMemo = Memo()
    private val mChildTasks = mutableListOf<Event>()
    override fun onInit(parent: View?) {
        VmManager(this).apply {
            memoViewModel = vmMemo
        }
        mMemo.type = arguments?.let {
            keyMaps[it.getString(MemoFragment.MEMO_TYPE, "")]
        } ?: 0

        editName.addTextChangedListener {
            mMemo.text = it.toString()
        }
        editPosition.addTextChangedListener {
            mMemo.position = it.toString()
        }

        var sDate = ""
        var sTime = ""
        var dpView: DatePicker? = null
        var timeView: TimePicker? = null
        editTime.setOnClickListener {
            MIUIDialog(activity()).show {
                customView(R.layout.view_datepicker) { dp ->
                    dpView = dp.datepicker
                }
                positiveButton(text = "确定") {
                    sDate = "${dpView?.datepicker?.year}-${dpView?.datepicker?.month}-${dpView?.datepicker?.dayOfMonth}"
                    cancel()
                    show {
                        customView(R.layout.view_timepicker) { tp ->
                            timeView = tp.timerpicker
                        }
                        positiveButton(text = "确定") {
                            sTime = "${timeView?.timerpicker?.currentHour}:${timeView?.timerpicker?.minute}"
                            toast("$sDate $sTime")
                            mMemo.time = Dates.getDate("$sDate $sTime", Dates.yyyyMMddHHmm).time
                        }
                        negativeButton(text = "取消") {
                            cancel()
                        }
                    }
                }
                negativeButton(text = "取消") {
                    cancel()
                }
            }
        }

        childTaskList.load(LinearLayoutManager(activity), BaseAdapter(mChildTasks, R.layout.item_memo_child_task, BR.task)) {
            it.setOnItemElementClickListener(object : BaseAdapter.OnItemElementClickListener(R.id.deleteTask) {
                override fun onClick(v: View?, position: Int) {
                    mChildTasks.removeAt(position)
                    it.notifyItemRemoved(position)
                }
            })
        }

        addChildTaskEvent.setOnClickListener {
            MIUIDialog(activity()).show {
                title(text = "添加子事件")
                input { charSequence, _ ->
                    mChildTasks.add(Event(content = charSequence.toString(), parentId = 0))
                    childTaskList.adapter?.notifyDataSetChanged()
                }
                positiveButton(text = "确定") {
                }
                negativeButton(text = "取消") { }
            }
        }

        saveMemo.setOnClickListener {
            mMemo.id = memoViewModel.addMemo(mMemo.apply {
                childEvent = mChildTasks
            })
            toast("添加成功~")
            close()
//            memoViewModel.memos.value = memoViewModel.memos.value?.plus(MemoWithEvent(mMemo, mMemo.childEvent))?.toMutableList()
        }


    }


}