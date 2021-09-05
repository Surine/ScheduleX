package cn.surine.schedulex.data.helper

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.DataMaps
import cn.surine.schedulex.base.utils.bitCount
import cn.surine.schedulex.base.utils.ifLess
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.ui.schedule_import_pro.model.mi_model.MiAiCourseInfo
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.model.mi_model.Section
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
        while (miAiCourseInfo.courses.isNotEmpty()) {
            for (index in miAiCourseInfo.courses.indices) {
                val info = miAiCourseInfo.courses[index]
                courseList.add(Course().apply {
                    this.scheduleId = scheduleId
                    id = generateId(scheduleId)
                    coureName = info.name
                    teacherName = info.teacher
                    teachingBuildingName = info.position
                    classDay = info.day.toString()
                    color = generateColor()
                    classWeek = generateBitWeek(info.weeks.split(",").map { it.toInt() }.toList(), Constants.MAX_WEEK)
                    //区间合并
                    var sectionsComp:MutableList<Section> = info.sections.split(",").map { Section(it.toInt()) }.toMutableList()
                    val start = sectionsComp[0]
                    var end = start
                    start.section *= -1  //标记位
                    for (i in 1 until sectionsComp.size) {
                        if (i < sectionsComp.size && sectionsComp[i].section - 1 == abs(end.section)) {
                            end = sectionsComp[i]
                            end.section *= -1
                        } else {
                            break
                        }
                    }
                    classSessions = abs(start.section).toString()
                    continuingSession = ifLess(abs(end.section) - abs(start.section) + 1, Constants.STAND_SESSION).toString()
                    sectionsComp = sectionsComp.filter { it.section > 0 }.toMutableList()
                    info.sections = sectionsComp.joinToString(separator = ",")
                    if (info.sections.isEmpty()) {
                        info.tag = true
                    }
                })
            }
            miAiCourseInfo.courses = miAiCourseInfo.courses.filter { !it.tag }.toMutableList()
        }
        block(courseList)
    }


    //TODO:super.cn adapter


    fun generateBitWeek(weeks: List<Int>, totalWeek: Int) = weeks.bitCount(totalWeek)

    private fun generateColor() = Constants.COLOR_1[Random().nextInt(Constants.COLOR_1.size)]

    private fun generateId(scheduleId: Long) = StringBuilder().run {
        append(scheduleId).append("@").append(UUID.randomUUID()).append(System.currentTimeMillis())
        toString()
    }


    /**
     * 兼容版course -> 存储版course
     * */
    fun wrapper2course(list: List<CourseWrapper>, scheduleId: Long): MutableList<Course> {
        val targetList = mutableListOf<Course>()
        list.forEach {
            val course = DataMaps.dataMappingCourseWrapper2Course(it)
            course.scheduleId = scheduleId
            course.id = generateId(scheduleId)
            course.color = generateColor()
            targetList.add(course)
        }
        return targetList
    }

}