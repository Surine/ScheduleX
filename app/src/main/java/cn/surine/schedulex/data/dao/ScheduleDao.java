package cn.surine.schedulex.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cn.surine.schedulex.data.entity.Schedule;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-25 20:10
 */
@Dao
public interface ScheduleDao {
    @Insert
    long insert(Schedule schedules);

    @Update
    void update(Schedule... schedules);

    @Delete
    void delete(Schedule... schedules);

    @Query("delete from schedule where roomId = :id")
    void delete(long id);

    @Query("delete from Schedule")
    void deleteAll();

    @Query("select * from Schedule")
    List<Schedule> getAll();

    @Query("select * from Schedule where roomId = :id")
    Schedule getById(long id);

}
