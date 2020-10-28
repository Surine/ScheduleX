package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser

import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.nodes.Element

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 17:48
 */
class NewUrpIntell : IntelliParser() {
    override fun parse(html: String): List<CourseWrapper> {
        return parse(html, listOf("table#courseTable", "td"))
    }


    override fun parseItem(element: Element): List<CourseWrapper> {
        val list = mutableListOf<CourseWrapper>()
        element.select("p").text().split(" ").windowed(5).forEach {
            list.add(CourseWrapper().apply { name = it.toString() })
        }
        println(list.map { it.name })
        return list
    }
}