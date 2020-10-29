package cn.surine.schedulex.ui.memo_pro

import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.data.entity.Memo
import cn.surine.schedulex.data.entity.MemoWithEvent

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:07
 */
class MemoViewModel(private val memoRepository: MemoRepository) : BaseViewModel() {

    fun addMemo(memo: Memo): Long {
        return memoRepository.addMemo(memo)
    }

    fun deleteMemoById(memoId: Long) {
        memoRepository.deleteMemoById(memoId)
    }

    val memos: MutableLiveData<MutableList<MemoWithEvent>> by lazy {
        MutableLiveData<MutableList<MemoWithEvent>>()
    }

    fun getMemos() {
        memos.value = memoRepository.getMemos().toMutableList()
    }
}

