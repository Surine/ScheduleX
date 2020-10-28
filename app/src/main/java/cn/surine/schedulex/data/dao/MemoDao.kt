package cn.surine.schedulex.data.dao

import androidx.room.*
import cn.surine.schedulex.data.entity.Memo

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 11:01
 */
@Dao
interface MemoDao {
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

    @Transaction
    @Query("select * from Memo")
    fun getAll(): List<Memo>

    @Query("select * from Memo where id = :id")
    fun getById(id: Long): Memo?

}