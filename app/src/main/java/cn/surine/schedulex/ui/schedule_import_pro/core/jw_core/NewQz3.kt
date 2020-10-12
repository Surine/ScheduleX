package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

/**
 * Intro：
 * 基于中南大学-新强智3
 * @author sunliwei
 * @date 10/12/20 15:39
 */
class NewQz3 : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("kbTable").getElementsByTag("tbody").first()
        val trs = target.getElementsByTag("tr")
        for (trIndex in 1..6) {
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0 || tds[tdIndex].children().size == 0) continue
                val courseInfos = tds[tdIndex].getElementsByTag("div")
                for (i in courseInfos) {
                    val text = i.attr("title").split("\n")
                    courseList.add(CourseWrapper().apply {
                        name = text[0].substringAfter("：")
                        teacher = text[1].substringAfter("：")
                        position = text[5].substringAfter("：")
                        day = ParseUtil.commonMap2[text[3].substringAfter("：")] ?: 1
                        sectionStart = trIndex * 2 - 1
                        sectionContinue = 2
                        val weekInfo = text[2].substringAfter("：")
                        if (weekInfo.contains(",") && weekInfo.contains("-")) {
                            val weekInfoCell = weekInfo.split(",")
                            val tmpWeek = mutableListOf<Int>()
                            for (w in weekInfoCell) {
                                if (w.contains("-") && w.contains("周")) {
                                    tmpWeek.addAll(ParseUtil.getWeekInfoByStr(w, singleRules = "%d-%d(单周)", doubleRules = "%d-%d(双周)", commonRules = "%d-%d(周)"))
                                } else if (w.contains("-")) {
                                    tmpWeek.addAll(ParseUtil.getWeekInfoByStr(w, commonRules = "%d-%d"))
                                } else {
                                    tmpWeek.add(w.toInt())
                                }
                            }
                            week = tmpWeek
                        } else if (weekInfo.contains("-")) {
                            week = ParseUtil.getWeekInfoByStr(weekInfo, singleRules = "%d-%d(单周)", doubleRules = "%d-%d(双周)", commonRules = "%d-%d(周)")
                        } else if (weekInfo.contains(",")) {
                            week = weekInfo.substringBefore("(").split(",").map { it.toInt() }
                        } else {
                            week = ParseUtil.getWeekInfoByStr(weekInfo, commonRules = "%d(周)")
                        }
                    })
                }
            }
        }
        return courseList
    }
}