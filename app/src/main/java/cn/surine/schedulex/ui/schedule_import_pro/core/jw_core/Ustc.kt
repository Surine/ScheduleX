package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * Intro：
 * USTC-USTCParser
 * @author sunliwei
 * @date 10/12/20 10:35
 */
class Ustc : IJWParse {

    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementsByClass("timetable").first().getElementsByTag("tbody")
        val morningBody = target[0]
        val noonBody = target[1]
        val nightBody = target[2]
        for (i in 1..5) {
            val tr = morningBody.getElementsByClass(i.toString())
            parseData(courseList, tr)
        }
        for (i in 6..10) {
            val tr = noonBody.getElementsByClass(i.toString())
            parseData(courseList, tr)
        }
        for (i in 11..13) {
            val tr = nightBody.getElementsByClass(i.toString())
            parseData(courseList, tr)
        }
        return courseList
    }

    private fun parseData(courseList: MutableList<CourseWrapper>, tr: Elements?) {
        tr ?: return
        val tds = tr.first().getElementsByTag("td")
        if (tds.size == 8) tds.removeAt(0)
        for (tdIndex in tds.indices) {
            if (tds[tdIndex].children().size != 0) {
                val divInfo = tds[tdIndex].getElementsByClass("cell").first().getElementsByClass("c")
                courseList.add(CourseWrapper().apply {
                    name = divInfo.first().getElementsByClass("title").text()
                    teacher = divInfo.first().getElementsByClass("teacher").text()
                    position = divInfo.first().getElementsByClass("classroom").text()
                    day = tdIndex + 1
                    val sessionInfo = divInfo.first().getElementsByClass("time").first().getElementsByClass("timespan").text().split(",")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo.size
                    val weekInfo = divInfo.first().getElementsByClass("time").first().getElementsByClass("week").text()
                    if (!weekInfo.contains("-") && !weekInfo.contains(",")) {
                        //单
                        week = listOf(weekInfo.toInt())
                    } else if (weekInfo.contains("-") && !weekInfo.contains(",")) {
                        //常规
                        week = ParseUtil.getWeekInfoByStr(target = weekInfo, commonRules = "%d-%d")
                    } else if (weekInfo.contains("-") && weekInfo.contains(",")) {
                        val weekValues = weekInfo.split(",")
                        val tmpWeek = mutableListOf<Int>()
                        for (i in weekValues) {
                            tmpWeek.addAll(ParseUtil.getWeekInfoByStr(target = i, commonRules = "%d-%d"))
                        }
                        week = tmpWeek
                    }
                })
            }
        }
    }
}