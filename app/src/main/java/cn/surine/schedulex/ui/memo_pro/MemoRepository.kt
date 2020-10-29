package cn.surine.schedulex.ui.memo_pro

import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.entity.Memo

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:08
 */
object MemoRepository : BaseRepository() {

    /**
     * add a memo to memo table
     * */
    fun addMemo(memo: Memo): Long {
        return appDatabase!!.memoDao().insert(memo)
    }

    /**
     * delete a memo by the memo id
     * */
    fun deleteMemoById(memoId: Long) {
        appDatabase!!.memoDao().delete(memoId)
    }


    /**
     * get all memos in the table
     * */
    fun getMemos(): List<Memo> {
        return emptyList()
//        return appDatabase!!.memoDao().getAll()
    }
}