package cn.surine.schedulex.ui.memo_pro

import android.animation.ObjectAnimator
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.net.toUri
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.ktx.open
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Memo
import kotlinx.android.synthetic.main.fragment_memo.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:26
 */
class MemoFragment : BaseFragment() {
    private lateinit var memoViewModel: MemoViewModel
    private var datas: List<Memo> = emptyList()
    override fun onInit(parent: View?) {

        VmManager(this).apply {
            memoViewModel = vmMemo
        }
        memoList.load(LinearLayoutManager(activity), BaseAdapter(datas, R.layout.item_memo, BR.memo)) {
            it.setOnItemClickListener {
                //click
            }
        }

        if (datas.isEmpty()) {
            memoViewModel.getMemos()
        }
        memoViewModel.memos.observe(this, Observer {
            datas = it
            memoList.adapter?.notifyDataSetChanged()
        })

        addMemo.setOnClickListener {
            open(R.id.action_memoFragment_to_addMemoFragment2)
        }

    }

    override fun layoutId() = R.layout.fragment_memo
}