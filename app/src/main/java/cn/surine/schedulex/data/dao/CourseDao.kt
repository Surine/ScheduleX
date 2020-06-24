package cn.surine.schedulex.data.dao

import androidx.room.*
import cn.surine.schedulex.data.entity.Course

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:49
 */
@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(course: Course?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg course: Course?)

    @Update
    fun update(vararg courses: Course?)

    @Delete
    fun delete(vararg courses: Course?)

    @Query("delete from Course where scheduleId = :scheduleId")
    fun delete(scheduleId: Long)

    @Query("delete from course")
    fun deleteAll()

    @get:Query("select * from course")
    val all: List<Course?>?

    @Query("select * from course where scheduleId = :scheduleId")
    fun getByScheduleId(scheduleId: Int): List<Course?>?

    @Query("select * from course where id = :id")
    fun getByCourseId(id: String?): Course?

    @Query("delete from Course where id = :id")
    fun deleteByCourseId(id: String?)

    @Query("select * from course where classDay = :day and scheduleId = :roomId order by classSessions")
    fun getTodayCourse(day: Int, roomId: Int): List<Course?>?
}