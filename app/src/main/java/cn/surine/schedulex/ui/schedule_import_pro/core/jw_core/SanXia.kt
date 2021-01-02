package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

//三峡大学
class SanXia : TableParser() {
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element ?: return emptyList()
        val ele = element.textNodes().windowed(3, 3)
        val list = mutableListOf<CourseWrapper>()
        for (i in ele) {
            list.add(CourseWrapper().apply {
                name = i[0].text()
                teacher = i[2].text()
                day = tdIndex
                sectionStart = trIndex * 2 - 1
                sectionContinue = 2
                val extraInfo = i[1].text().split(" ")
                position = extraInfo[0]
                week = ParseUtil.getWeekInfoByStr("${extraInfo[1]}${if (extraInfo.size == 3) extraInfo[2] else null}", commonRules = "%d-%d周", singleRules = "%d-%d周单周", doubleRules = "%d-%d周双周")
            })
        }
        return list
    }

    override fun getTargetInitFunc(html: String): Element = Jsoup.parse(html).getElementsByClass("GridViewStyle")[0]

    override fun skipRow() = 1

    override fun skipCol() = 1

}