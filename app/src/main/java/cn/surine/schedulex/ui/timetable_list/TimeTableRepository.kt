package cn.surine.schedulex.ui.timetable_list

import cn.surine.schedulex.base.controller.BaseRepository
import cn.surine.schedulex.data.entity.TimeTable

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 10:13
 */
object TimeTableRepository : BaseRepository() {
    /**
     * 获取所有时间表
     */
    fun getAllTimeTables(): List<TimeTable> {
        return appDatabase.timeTableDao()!!.all ?: ArrayList()
    }


    /**
     * 插入一份时间表
     */
    fun insertTimeTable(timeTable: TimeTable?) {
        appDatabase.timeTableDao()!!.insert(timeTable)
    }

    /**
     * 通过id查询时间表
     */
    fun getTimeTableById(id: Long): TimeTable? {
        return appDatabase.timeTableDao()!!.getById(id)
    }

    /**
     * 更新时间表
     */
    fun updateTimeTable(timeTable: TimeTable?) {
        appDatabase.timeTableDao()!!.update(timeTable)
    }

    fun deleteTimeTableById(roomId: Int) {
        appDatabase.timeTableDao()!!.delete(roomId.toLong())
    }
}