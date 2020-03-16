package cn.surine.schedulex.ui.timetable_list;

import android.os.Bundle;

import java.util.List;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.data.entity.TimeTable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:16
 */
public class TimeTableRepository extends BaseRepository {
    public static AbstractSingleTon<TimeTableRepository> abt = new AbstractSingleTon<TimeTableRepository>() {
        @Override
        protected TimeTableRepository newObj(Bundle bundle) {
            return new TimeTableRepository();
        }
    };


    /**
     * 获取所有时间表
     */
    public List<TimeTable> getAllTimeTables() {
        return appDatabase.timeTableDao().getAll();
    }


    /**
     * 插入一份时间表
     */
    public void insertTimeTable(TimeTable timeTable) {
        appDatabase.timeTableDao().insert(timeTable);
    }

    /**
     * 通过id查询时间表
     */
    public TimeTable getTimeTableById(long id) {
        return appDatabase.timeTableDao().getById(id);
    }

    /**
     * 更新时间表
     * */
    public void updateTimeTable(TimeTable timeTable) {
        appDatabase.timeTableDao().update(timeTable);
    }

    public void deleteTimeTableById(int roomId) {
        appDatabase.timeTableDao().delete(roomId);
    }
}
