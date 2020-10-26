package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.nodes.Element

/**
 * Intro：
 * 通用型 table parser
 * @author sunliwei
 * @date 10/19/20 15:17
 */
abstract class TableParser : IJWParse {

    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = getTargetInitFunc(html)
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex < skipRow()) continue
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (trIndex < skipCol()) continue
                courseList.addAll(itemParse(tds[tdIndex], trIndex, tdIndex))
            }
        }
        return courseList
    }

    abstract fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper>
    abstract fun getTargetInitFunc(html: String): Element
    abstract fun skipRow(): Int
    abstract fun skipCol(): Int
}