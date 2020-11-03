package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * WTU GRADUATES
 * @author sunliwei
 * @date 11/3/20 14:22
 */
class WtuGraduates : TableParser() {

    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        val list = mutableListOf<CourseWrapper>()
        if (element == null || !element.hasAttr("rowspan")) return emptyList()
        val p = element.getElementsByTag("div").first().getElementsByTag("p").text().split(" ")
        list.add(CourseWrapper().apply {
            name = p[0]
            teacher = p[2]
            position = p[3]
            week = ParseUtil.getWeekInfoByStr(p[1], commonRules = "%d-%d周")
            day = ParseUtil.commonMap3[element.attr("w")] ?: 1
            sectionStart = trIndex
            sectionContinue = element.attr("rowspan").toIntOrNull() ?: 2
        })
        return list
    }

    override fun getTargetInitFunc(html: String): Element = Jsoup.parse(html).getElementsByClass("curriculum").first().getElementsByTag("tbody").first()

    override fun skipRow() = 1

    override fun skipCol() = 1
}