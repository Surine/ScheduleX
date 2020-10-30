package cn.surine.schedulex.ui.memo_pro

import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.entity.Event
import cn.surine.schedulex.data.entity.Memo
import cn.surine.schedulex.data.entity.MemoWithEvent

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
        val id = appDatabase!!.memoDao().insert(memo)
        memo.childEvent.forEach {
            addMemoChild(it.apply {
                parentId = id
            })
        }
        return id
    }

    private fun addMemoChild(event: Event): Long {
        return appDatabase!!.eventDao().insert(event)
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
    fun getMemos(): List<MemoWithEvent> {
        return appDatabase!!.memoDao().getAll()
    }

    fun getMemoById(id:Long): MemoWithEvent {
        return appDatabase!!.memoDao().getMemoById(id)
    }
}