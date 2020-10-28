package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/27/20 17:24
 */
abstract class IntelliParser : IJWParse {
    fun parse(html: String, rules: List<String>): List<CourseWrapper> {
        val queue = LinkedList<Element?>()
        Jsoup.parse(html).select(rules[0]).forEach {
            queue.offerLast(it)
        }
        for (i in 1 until rules.size) {
            val queueSize = queue.size
            var index = 0
            while (index < queueSize) {
                queue.pollFirst()?.select(rules[i])?.forEach {
                    queue.offerLast(it)
                }
                index++
            }
        }

        val courseList = mutableListOf<CourseWrapper>()
        while (queue.isNotEmpty()) {
            val element = queue.pollFirst() ?: continue
            courseList.addAll(parseItem(element))
//            println("${element.parent().elementSiblingIndex()} | ${element.elementSiblingIndex()} |  ${element.textNodes().windowed(4)}")
        }
        return courseList
    }


    abstract fun parseItem(element: Element): List<CourseWrapper>

}
