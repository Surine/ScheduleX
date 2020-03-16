package cn.surine.schedulex.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cn.surine.schedulex.data.entity.TimeTable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:23
 */

@Dao
public interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TimeTable timeTable);

    @Update
    void update(TimeTable... timeTables);

    @Delete
    void delete(TimeTable... timeTables);

    @Query("delete from TimeTable where roomId = :id")
    void delete(long id);

    @Query("select * from TimeTable")
    List<TimeTable> getAll();

    @Query("select * from TimeTable where roomId = :id")
    TimeTable getById(long id);
}
