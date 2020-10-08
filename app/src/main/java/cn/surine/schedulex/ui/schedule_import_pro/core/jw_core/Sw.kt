package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 22:37
 */
class Sw :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("manualArrangeCourseTable").getElementsByTag("tbody").first()
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            //每一行的列
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0 || tds[tdIndex].text().isEmpty()) continue  //跳过时间列
                val tdData = tds[tdIndex]
                if (tdData.className().isNotEmpty()) {
                    var nodes = tdData.textNodes()
                    if (nodes.size % 2 == 0) {
                        while (nodes.size != 0) {
                            courseList.add(CourseWrapper().apply {
                                teacher = nodes[0].text().split(" ")[0]
                                sectionContinue = tdData.attr("rowspan").toInt()
                                //竖向排列
                                val idData = tdData.id().substringBefore("_").removePrefix("TD")
                                day = idData.toInt() / 12 + 1
                                sectionStart = idData.toInt() % 12 + 1
                                //包含逗号的才是有地点的课，否则略过
                                if (nodes[1].text().contains(",")) {
                                    position = nodes[1].text().split(",")[1].removeSuffix(")")
                                }
                                name = nodes[0].text().removePrefix(teacher).trim().substringBeforeLast("(")
                                //周信息
                                val tmpWeek = mutableListOf<Int>()
                                val weekInfo =
                                        if (nodes[1].text().contains(",")) {
                                            nodes[1].text().split(",")[0].removePrefix("(").split(" ")
                                        } else {
                                            nodes[1].text().removePrefix("(").removeSuffix(")").split(" ")
                                        }
                                for (i in weekInfo) {
                                    if (i.contains("-")) {
                                        tmpWeek.addAll(ParseUtil.getWeekInfoByStr(i,
                                                commonRules = "%d-%d"
                                        ))
                                    } else {
                                        tmpWeek.add(i.toInt())
                                    }
                                }
                                week = tmpWeek
                            })
                            nodes = nodes.subList(2, nodes.size)
                        }
                    }
                }
            }
        }
        return courseList
    }

}