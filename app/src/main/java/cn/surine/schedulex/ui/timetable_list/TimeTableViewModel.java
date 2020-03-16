package cn.surine.schedulex.ui.timetable_list;

import androidx.lifecycle.ViewModel;

import java.util.List;

import cn.surine.schedulex.data.entity.TimeTable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:16
 */
public class TimeTableViewModel extends ViewModel {
    private TimeTableRepository timeTableRepository;

    public TimeTableViewModel(TimeTableRepository timeTableRepository) {
        this.timeTableRepository = timeTableRepository;
    }


    public List<TimeTable> getAllTimeTables() {
        return timeTableRepository.getAllTimeTables();
    }


    /**
     * 获取时间表
     */
    public TimeTable getTimTableById(long i) {
        return timeTableRepository.getTimeTableById(i);
    }


    /**
     * 添加时间表
     */
    public void addTimeTable(TimeTable timeTable) {
        timeTableRepository.insertTimeTable(timeTable);
    }


    /**
     * 更新时间表
     */
    public void updateTimeTable(TimeTable timeTable) {
        timeTableRepository.updateTimeTable(timeTable);
    }


    /**
     * 删除时间表
     */
    public void deleteTimeTableById(int roomId) {
        timeTableRepository.deleteTimeTableById(roomId);
    }
}
