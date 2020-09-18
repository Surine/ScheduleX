package cn.surine.schedulex.data.helper

import cn.surine.coursetableview.entity.BTimeTable
import cn.surine.schedulex.base.utils.DataMaps
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.ui.course.CourseRepository
import cn.surine.schedulex.ui.schedule.ScheduleRepository
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository
import java.lang.Exception
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 15:16
 */
object DataHandler {
    private val courseRepository = CourseRepository.abt.instance
    private val scheduleRepository = ScheduleRepository.abt.instance
    private val timeTableRepository = TimeTableRepository

    /**
     * 获取某天的课程
     *
     * @param day      星期几？
     * @param nextWeek 是否下周？
     */
    fun getCourseList(day: Int, nextWeek: Boolean): List<Course> {
        val curSchedule = scheduleRepository.curSchedule
        try {
            return courseRepository.getTodayCourseListByScheduleId(day, if (nextWeek) curSchedule.curWeek() + 1 else curSchedule.curWeek(), curSchedule.roomId).sortedWith(kotlin.Comparator { o1, o2 ->
                o1.classSessions.toInt() - o2.classSessions.toInt()
            })
        }catch (e:Exception){

        }
        return emptyList()
    }

    /**
     * 获取当前课表的时间表
     */
    fun getCurTimeTable(): BTimeTable {
        val curSchedule = scheduleRepository.curSchedule
        return DataMaps.dataMappingTimeTableToBTimeTable(timeTableRepository.getTimeTableById(curSchedule.timeTableId))
    }


    /**
     * 获取某周课程
     *
     * @param nextWeek 周
     */
    fun getWeekCourse(nextWeek: Boolean): List<Course> {
        val schedule = scheduleRepository.curSchedule
        val curWeek = schedule.curWeek()
        return courseRepository.getWeekCourseListByScheduleId(if (nextWeek) curWeek + 1 else curWeek, schedule.roomId)
    }

    /**
     * 获得当前课表的最大节次
     */
    fun getCurMaxSession(): Int {
        return scheduleRepository.curSchedule.maxSession
    }

    /**
     * 根据提供的课程数据获取某一天的课程
     */
    fun getOneDayCourse(day: Int, courses: List<Course>): List<Course> {
        val handlers: MutableList<Course> = ArrayList()
        for (i in courses.indices) {
            val course = courses[i]
            if (course.classDay == day.toString()) {
                handlers.add(course)
            }
        }
        return handlers
    }


    fun getCurSchedule(): Schedule? {
        return scheduleRepository.curSchedule
    }
}