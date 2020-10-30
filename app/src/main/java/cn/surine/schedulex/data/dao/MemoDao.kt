package cn.surine.schedulex.data.dao

import androidx.room.*
import cn.surine.schedulex.data.entity.Memo
import cn.surine.schedulex.data.entity.MemoWithEvent

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:01
 */
@Dao
interface MemoDao {
    /**
     * insert a memo
     * @param memos Memo
     * */
    @Insert
    fun insert(memos: Memo?): Long

    @Update
    fun update(vararg memos: Memo?)

    @Delete
    fun delete(vararg memos: Memo?)

    @Query("delete from Memo where id = :id")
    fun delete(id: Long)

    @Query("delete from Memo")
    fun deleteAll()


    @Transaction    //原子性操作注解
    @Query("select * from Memo")
    fun getAll(): List<MemoWithEvent>

    @Transaction
    @Query("select * from Memo where id = :id")
    fun getMemoById(id: Long): MemoWithEvent

}