package cn.surine.schedulex.ui.schedule;

import android.os.Bundle;

import java.util.List;

import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.data.entity.Schedule;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-25 20:03
 */
public class ScheduleRepository extends BaseRepository {

    public static AbstractSingleTon<ScheduleRepository> abt = new AbstractSingleTon<ScheduleRepository>() {
        @Override
        protected ScheduleRepository newObj(Bundle bundle) {
            return new ScheduleRepository();
        }
    };

    /**
     * 添加课表
     */
    public long addSchedule(Schedule schedule) {
        return appDatabase.scheduleDao().insert(schedule);
    }

    public Schedule getScheduleById(long id) {
        return appDatabase.scheduleDao().getById(id);
    }


    public void deleteScheduleById(long scheduleId) {
        appDatabase.scheduleDao().delete(scheduleId);
    }

    /**
     * 获取当前选中课表
     * */
    public Schedule getCurSchedule() {
        long curScheduleId =  Prefs.getLong(Constants.CUR_SCHEDULE,-1L);
        return getScheduleById(curScheduleId);
    }

    /**
     * 获取所有课表
     *
     * @return*/
    public List<Schedule> getSchedules() {
        return appDatabase.scheduleDao().getAll();
    }


    /**
     * 更新课表
     * @param schedule 课表
     */
    public void updateSchedule(Schedule schedule) {
        appDatabase.scheduleDao().update(schedule);
    }
}
