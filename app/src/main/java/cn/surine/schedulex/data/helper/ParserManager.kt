package cn.surine.schedulex.data.helper

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.bitCount
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.miai_import.MiAiCourseInfo
import java.util.*

/**
 * Intro：
 * 通用映射器
 * @author sunliwei
 * @date 9/9/20 10:57
 */
object ParserManager {
    //小爱解析
    fun aiParser(scheduleId: Long, miAiCourseInfo: MiAiCourseInfo, block: (List<Course>) -> Unit) {
        val courseList = mutableListOf<Course>()
        while (miAiCourseInfo.courseInfos.isNotEmpty()) {
            for (info in miAiCourseInfo.courseInfos) {
                courseList.add(Course().apply {
                    this.scheduleId = scheduleId
                    id = generateId(scheduleId)
                    coureName = info.name
                    teacherName = info.teacher
                    teachingBuildingName = info.position
                    classDay = info.day.toString()
                    color = generateColor()
                    classWeek = generateBitWeek(info.weeks, miAiCourseInfo.totalWeek)
                    //区间合并
                    val start = info.sections[0]
                    var end = start
                    for (i in 1 until info.sections.size) {
                        val sec = info.sections[i]
                        if (i + 1 < info.sections.size && info.sections[i + 1].section - 1 == sec.section) {
                            end = info.sections[i + 1]
                            end.section *= -1
                        } else {
                            break
                        }
                    }
                    classSessions = start.toString()
                    continuingSession = (if (start.section - end.section + 1 > Constants.STAND_SESSION) Constants.STAND_SESSION else start.section - end.section + 1).toString()
                    info.sections = info.sections.filter { it.section > 0 }.toMutableList()
                    if (info.sections.isEmpty()) {
                        miAiCourseInfo.courseInfos.remove(info)
                    }
                })
            }
        }
        block(courseList)
    }

    private fun generateBitWeek(weeks: List<Int>, totalWeek: Int) = weeks.bitCount(totalWeek)

    private fun generateColor() = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]

    private fun generateId(scheduleId: Long) = StringBuilder().run {
        append(scheduleId).append("@").append(UUID.randomUUID()).append(System.currentTimeMillis())
        toString()
    }

}