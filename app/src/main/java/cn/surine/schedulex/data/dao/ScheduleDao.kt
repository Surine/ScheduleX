package cn.surine.schedulex.data.dao

import androidx.room.*
import cn.surine.schedulex.data.entity.Schedule

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-25 20:10
 */
@Dao
interface ScheduleDao {
    @Insert
    fun insert(schedules: Schedule?): Long

    @Update
    fun update(vararg schedules: Schedule?)

    @Delete
    fun delete(vararg schedules: Schedule?)

    @Query("delete from schedule where roomId = :id")
    fun delete(id: Long)

    @Query("delete from Schedule")
    fun deleteAll()

    @get:Query("select * from Schedule")
    val all: List<Schedule?>?

    @Query("select * from Schedule where roomId = :id")
    fun getById(id: Long): Schedule?

    @get:Query("select count(*) from Schedule")
    val totalNum: Int
}