package cn.surine.schedulex.ui.schedule_import_pro.util

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.model.compat.Course

/**
 * Intro：
 *  parse util
 * @author sunliwei
 * @date 9/21/20 10:46
 */
object ParseUtil {

    const val SESSION = "节"
    const val WEEK = "周"

    val weekList = listOf("周一","周二","周三","周四","周五","周六","周日")
    val commonMap = mapOf("周一" to 1, "周二" to 2, "周三" to 3, "周四" to 4, "周五" to 5, "周六" to 6, "周日" to 7)
    val commonMap2 = mapOf("星期一" to 1, "星期二" to 2, "星期三" to 3, "星期四" to 4, "星期五" to 5, "星期六" to 6, "星期日" to 7)
    val commonMap3 = mapOf("Monday" to 1, "Tuesday" to 2, "Wednesday" to 3, "Thursday" to 4, "Friday" to 5, "Saturday" to 6, "Sunday" to 7)

    /**
     * 根据字符串返回目标
     * @param target 字符串
     * @param baseData 基础模板
     * */
    fun getDayInfoByStr(target: String, baseData: Map<String, Int> = commonMap) = commonMap[target]
            ?: 1


    /**
     * 根据模板来返回解析出来的周信息
     * 适用于 区间范围 形式的字符串
     * @param target 待解析字符串
     * @param singleRules 单周模板
     * @param doubleRules 双周模板
     * @param commonRules 默认模板（全周）
     * 如果对应的三种情况是一样的，（也就是不存在xxx单周或者xxx双周类似的信息，可以直接忽略传递参数，仅传递commonRules）
     * */
    fun getWeekInfoByStr(target: String, singleRules: String = "", doubleRules: String = "", commonRules: String): List<Int> {
        when {
            '单' in target -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(singleRules, i, j) == target) {
                            return (i..j).toList().filter { it % 2 != 0 }
                        }
                    }
                }
            }
            '双' in target -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(doubleRules, i, j) == target) {
                            return (i..j).toList().filter { it % 2 == 0 }
                        }
                    }
                }
            }
            else -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(commonRules, i, j) == target) {
                            return (i..j).toList()
                        }
                    }
                }
            }
        }
        return emptyList()
    }

    //compat version
    private fun converter(course: Course): CourseWrapper {
        val courseWrapper = CourseWrapper()
        courseWrapper.name = course.name
        courseWrapper.position = course.room
        courseWrapper.teacher = course.teacher
        courseWrapper.day = course.day
        courseWrapper.sectionStart = course.startNode
        courseWrapper.sectionContinue = course.endNode - course.startNode + 1
        for (i in course.startWeek..course.endWeek) {
            when (course.type) {
                0 -> courseWrapper.week = courseWrapper.week.plus(i)
                1 -> if (i % 2 == 1) courseWrapper.week = courseWrapper.week.plus(i)
                2 -> if (i % 2 == 0) courseWrapper.week = courseWrapper.week.plus(i)
            }
        }
        return courseWrapper
    }

    fun wrap(courseList: ArrayList<Course>): List<CourseWrapper> {
        val data = mutableListOf<CourseWrapper>()
        for (course in courseList)
            data.add(converter(course = course))
        return data
    }

}