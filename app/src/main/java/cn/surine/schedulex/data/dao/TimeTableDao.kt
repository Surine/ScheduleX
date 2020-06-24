package cn.surine.schedulex.data.dao

import androidx.room.*
import cn.surine.schedulex.data.entity.TimeTable

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:23
 */
@Dao
interface TimeTableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(timeTable: TimeTable?): Long

    @Update
    fun update(vararg timeTables: TimeTable?)

    @Delete
    fun delete(vararg timeTables: TimeTable?)

    @Query("delete from TimeTable where roomId = :id")
    fun delete(id: Long)

    @get:Query("select * from TimeTable")
    val all: List<TimeTable>?

    @Query("select * from TimeTable where roomId = :id")
    fun getById(id: Long): TimeTable?
}