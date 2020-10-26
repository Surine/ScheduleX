package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * new url based on tust
 * @author sunliwei
 * @date 10/26/20 11:33
 */
class NewUrp : TableParser() {
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element ?: return emptyList()
        if (element.childNodeSize() == 0) return emptyList()
        val list = mutableListOf<CourseWrapper>()
        val info = element.children()
        for (i in info) {
            val children = i.allElements
            list.add(CourseWrapper().apply {
                name = children[1].text()
                teacher = children[2].text()
                position = children[5].text()
                day = tdIndex + 1
                val sessionInfo = children[4].text().substringBefore("节").split("-")
                sectionStart = sessionInfo[0].toInt()
                sectionContinue = sessionInfo[1].toInt() - sectionStart + 1
                week = ParseUtil.getWeekInfoByStr(children[3].text(), singleRules = "%d-%d周单", doubleRules = "%d-%d周双", commonRules = "%d-%d周")
            })
        }
        return list
    }

    override fun getTargetInitFunc(html: String) = Jsoup.parse(html).getElementById("courseTable").getElementsByTag("tbody").first()!!
    override fun skipRow() = 0
    override fun skipCol() = 0

}