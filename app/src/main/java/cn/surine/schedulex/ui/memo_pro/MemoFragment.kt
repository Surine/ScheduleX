package cn.surine.schedulex.ui.memo_pro

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.controller.ViewHolder
import cn.surine.schedulex.base.ktx.open
import cn.surine.schedulex.base.ktx.toast
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.MemoWithEvent
import cn.surine.schedulex.ui.view.custom.helper.ItemDecoration
import com.peanut.sdk.miuidialog.MIUIDialog
import kotlinx.android.synthetic.main.fragment_memo.*
import kotlinx.android.synthetic.main.item_memo.view.*
import kotlinx.android.synthetic.main.view_add_memo_type.view.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:26
 */
class MemoFragment : BaseFragment() {

    companion object {
        const val MEMO_TYPE = "MEMO_TYPE"
        const val MEMO_ID = "MEMO_ID"
    }

    private lateinit var memoViewModel: MemoViewModel
    private var datas: MutableList<MemoWithEvent> = mutableListOf()

    override fun onInit(parent: View?) {
        VmManager(this).apply {
            memoViewModel = vmMemo
        }

        val adapter = MemoAdapter(activity(), datas, R.layout.item_memo, BR.memo)
        memoList.load(LinearLayoutManager(activity), adapter)
        memoList.addItemDecoration(ItemDecoration())
        adapter.setOnItemClickListener {
            open(R.id.action_memoFragment_to_memoInfoFragment2, Bundle().apply {
                putLong(MEMO_ID, datas[it].memo.id)
            })
        }

        memoViewModel.getMemos()
        memoViewModel.memos.observe(this, Observer {
            datas.clear()
            datas.addAll(it)
            memoList.adapter?.notifyDataSetChanged()
        })

        addMemo.setOnClickListener {
            MIUIDialog(activity()).show {
                customView(R.layout.view_add_memo_type) { layout ->
                    layout.addNormal.setOnClickListener { onClick(it);cancel() }
                    layout.addTrip.setOnClickListener { onClick(it);cancel() }
                    layout.addMeeting.setOnClickListener { onClick(it);cancel() }
                    layout.addExam.setOnClickListener { onClick(it);cancel() }
                    layout.addOther.setOnClickListener { onClick(it);cancel() }
                }
            }
        }


    }


    private fun onClick(view: View) {
        open(R.id.action_memoFragment_to_addMemoFragment2, Bundle().apply {
            putString(MEMO_TYPE, view.tag as String?)
        })
    }


    override fun layoutId() = R.layout.fragment_memo
}

class MemoAdapter(val context: Context, private val list: List<MemoWithEvent>, layoutId: Int, bindName: Int) : BaseAdapter<MemoWithEvent>(list, layoutId, bindName) {
    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        for ((index, i) in list[position].events.withIndex()) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_child_event_display, null)
            val checkBox = view.findViewById<CheckBox>(R.id.taskInfo)
            checkBox.isChecked = i.done
            checkBox.text = i.content
            checkBox.setOnCheckedChangeListener { view, isChecked ->
                toast("$position-$index-$isChecked")
                if (isChecked) {
                    view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            holder.itemView.childEvent.addView(view)
        }
    }
}