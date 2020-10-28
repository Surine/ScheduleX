package cn.surine.schedulex.ui.memo_pro

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.surine.schedulex.BR
import cn.surine.schedulex.R
import cn.surine.schedulex.app_base.VmManager
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.base.controller.BaseFragment
import cn.surine.schedulex.base.utils.load
import cn.surine.schedulex.data.entity.Memo
import kotlinx.android.synthetic.main.fragment_memo.*
import kotlinx.android.synthetic.main.view_recycle_list.view.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:26
 */
class MemoFragment : BaseFragment() {
    private lateinit var memoViewModel: MemoViewModel
    private lateinit var datas: List<Memo>
    override fun onInit(parent: View?) {
        VmManager(this).apply {
            memoViewModel = vmMemo
        }
        memoList.recyclerview.load(LinearLayoutManager(activity), BaseAdapter(datas, R.layout.item_memo,BR.memo)) {
            it.setOnItemClickListener {
                //click
            }
        }

        if (datas.isEmpty()) {
            memoViewModel.getMemos()
        }
        memoViewModel.memos.observe(this, Observer {
            memoList.recyclerview.adapter
        })

        addMemo.setOnClickListener {
            //add
        }

    }

    override fun layoutId() = R.layout.fragment_memo
}