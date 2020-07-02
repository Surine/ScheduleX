package cn.surine.schedulex.ui.timetable_list

import androidx.annotation.Keep
import cn.surine.schedulex.base.controller.BaseViewModel
import cn.surine.schedulex.data.entity.TimeTable

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-05 21:16
 */
@Keep
class TimeTableViewModel(private val timeTableRepository: TimeTableRepository) : BaseViewModel() {
    val allTimeTables: List<TimeTable>
        get() = timeTableRepository.getAllTimeTables()

    /**
     * 获取时间表
     */
    fun getTimTableById(i: Long): TimeTable? {
        return timeTableRepository.getTimeTableById(i)
    }

    /**
     * 添加时间表
     */
    fun addTimeTable(timeTable: TimeTable?) {
        timeTableRepository.insertTimeTable(timeTable)
    }

    /**
     * 更新时间表
     */
    fun updateTimeTable(timeTable: TimeTable?) {
        timeTableRepository.updateTimeTable(timeTable)
    }

    /**
     * 删除时间表
     */
    fun deleteTimeTableById(roomId: Int) {
        timeTableRepository.deleteTimeTableById(roomId)
    }

}