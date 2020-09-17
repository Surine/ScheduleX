package cn.surine.schedulex.data.helper

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.bitCount
import cn.surine.schedulex.base.utils.ifGreater
import cn.surine.schedulex.base.utils.ifLess
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.miai_import.model.MiAiCourseInfo
import java.util.*
import kotlin.math.abs

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
            val deleteTag = mutableListOf<Int>()
            for (index in miAiCourseInfo.courseInfos.indices) {
                val info = miAiCourseInfo.courseInfos[index]
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
                    start.section *= -1  //标记位
                    for (i in 1 until info.sections.size) {
                        if (i < info.sections.size && info.sections[i].section - 1 == abs(end.section)) {
                            end = info.sections[i]
                            end.section *= -1
                        } else {
                            break
                        }
                    }
                    classSessions = abs(start.section).toString()
                    continuingSession = ifLess(abs(end.section) - abs(start.section) + 1,Constants.STAND_SESSION).toString()
                    info.sections = info.sections.filter { it.section > 0 }.toMutableList()
                    if (info.sections.isEmpty()) {
                        info.tag = true
                    }
                })
            }
            miAiCourseInfo.courseInfos = miAiCourseInfo.courseInfos.filter { !it.tag }.toMutableList()
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