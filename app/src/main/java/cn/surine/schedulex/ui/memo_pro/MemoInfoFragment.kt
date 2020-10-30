package cn.surine.schedulex.ui.memo_pro

import android.graphics.Paint
import android.view.LayoutInflater
import android.widget.CheckBox
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseBindingFragment
import cn.surine.schedulex.data.entity.MemoWithEvent
import cn.surine.schedulex.databinding.FragmentMemoInfoBinding
import kotlinx.android.synthetic.main.fragment_memo_info.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/28/20 16:43
 */
class MemoInfoFragment : BaseBindingFragment<FragmentMemoInfoBinding>() {
    private lateinit var memoViewModel: MemoViewModel
    override fun layoutId() = R.layout.fragment_memo_info
    private lateinit var curMemo: MemoWithEvent
    override fun onInit(t: FragmentMemoInfoBinding?) {
        VmManager(this).apply {
            memoViewModel = vmMemo
        }
        arguments?.let {
            curMemo = memoViewModel.getMemoById(it.getLong(MemoFragment.MEMO_ID))
            t?.memo = curMemo
        }

        for (i in curMemo.events) {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_child_event_display, null)
            val checkBox = view.findViewById<CheckBox>(R.id.taskInfo)
            checkBox.isChecked = i.done
            checkBox.text = i.content
            childTaskContainer.addView(view)
            checkBox.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

}