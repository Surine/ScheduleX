package cn.surine.schedulex.ui.memo_pro

import androidx.lifecycle.MutableLiveData
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.data.entity.Memo

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:07
 */
class MemoViewModel(private val memoRepository: MemoRepository) : BaseViewModel() {
    fun addMemo(memo: Memo) {
        memoRepository.addMemo(memo)
    }

    fun deleteMemoById(memoId: Long) {
        memoRepository.deleteMemoById(memoId)
    }

    val memos: MutableLiveData<List<Memo>> by lazy {
        MutableLiveData<List<Memo>>()
    }

    fun getMemos() {
        memos.value = memoRepository.getMemos()
    }
}

