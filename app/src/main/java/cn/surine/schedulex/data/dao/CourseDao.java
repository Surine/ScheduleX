package cn.surine.schedulex.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:49
 */

@Dao
public interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Course course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course ...course);


    @Update
    void update(Course... courses);

    @Delete
    void delete(Course... courses);

    @Query("delete from Course where scheduleId = :scheduleId")
    void delete(long scheduleId);

    @Query("delete from course")
    void deleteAll();

    @Query("select * from course")
    List<Course> getAll();


    @Query("select * from course where scheduleId = :scheduleId")
    List<Course> getByScheduleId(int scheduleId);


    @Query("select * from course where id = :id")
    Course getByCourseId(String id);


    @Query("delete from Course where id = :id")
    void deleteByCourseId(String id);


    @Query("select * from course where classDay = :day and scheduleId = :roomId")
    List<Course> getTodayCourse(int day,int roomId);
}
