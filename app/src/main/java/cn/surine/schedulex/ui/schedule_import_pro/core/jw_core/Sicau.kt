package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * 四川农业大学
 * */
class Sicau : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val t = Jsoup.parse(html).getElementsByTag("table")
        val target = t[t.lastIndex - 1]
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex <= 1) continue
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                //去除无效行
                if (tds.size == 8) {
                    if (tdIndex == 0) continue
                } else if (tds.size == 9) {
                    if (tdIndex <= 1) continue
                }
                val nodes = tds[tdIndex].textNodes()
                if (nodes.size <= 1) continue
                courseList.add(CourseWrapper().apply {
                    name = nodes[0].text().split("：")[0]
                    teacher = nodes[0].text().split("：")[1]
                    position = nodes[1].toString()
                    day = if (tds.size == 8) tdIndex else (tdIndex - 1)
                    sectionStart = 2 * (trIndex - 1) - 1
                    sectionContinue = 2
                    week = getWeek(tds[tdIndex], nodes[2])
                })
            }
        }
        return courseList
    }

    private fun getWeek(element: Element?, textNode: TextNode): List<Int> {
        element ?: return emptyList()
        val targetStr = textNode.text().substringBefore("(")
        if (targetStr.contains("-")) {
            return when {
                element.text().contains("单") -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周").filter { it % 2 == 1 }
                }
                element.text().contains("双") -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周").filter { it % 2 == 0 }
                }
                else -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周")
                }
            }
        } else {
            return targetStr.removeSuffix("周").split(",").map {
                it.toInt()
            }.toList()
        }
    }
}