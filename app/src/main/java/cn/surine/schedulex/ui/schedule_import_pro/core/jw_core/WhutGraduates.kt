package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/19/20 14:36
 */
class WhutGraduates : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementsByClass("datagrid-btable").first()
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0) continue
                val content = tds[tdIndex].getElementsByTag("div").first()
                val node = content.textNodes()
                if (node.size == 0) continue
                courseList.add(CourseWrapper().apply {
                    name = node[0].text()
                    teacher = node[1].text()
                    val sessionInfo = node[2].text().substringAfter(":").substringBefore("节").split("-")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo[1].toInt() - sectionStart + 1
                    week = ParseUtil.getWeekInfoByStr(node[3].text().substringAfter(":"), commonRules = "%d-%d")
                    position = node[4].text()
                    day = tdIndex
                })
            }
        }
        return courseList
    }
}
