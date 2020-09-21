package cn.surine.schedulex.third_parse.new_version.parses

import cn.surine.schedulex.third_parse.new_version.bean.CourseWrapper
import cn.surine.schedulex.third_parse.new_version.helper.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * 默认正方解析器
 * @author sunliwei
 * @date 9/21/20 15:26
 */
open class Zf : IParse {
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("Table1")
        //表格行
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex <= 1) continue  //清除表头（包括早晨一行）
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                parseInfo(tds[tdIndex], courseList)
            }
        }
        return courseList
    }

    open fun parseInfo(element: Element, courseList: MutableList<CourseWrapper>) {
        var nodes = element.textNodes()
        if (nodes.size % 4 == 0) {
            while (nodes.size != 0) {
                courseList.add(CourseWrapper().apply {
                    name = nodes[0].text()
                    position = nodes[3].text()
                    teacher = nodes[2].text()
                    day = ParseUtil.getDayInfoByStr(nodes[1].text().substring(0, 2))
                    week = ParseUtil.getWeekInfoByStr(arrayOf("第%d-%d周|单周}", "第%d-%d周|双周}", "第%d-%d周}"), nodes[1].text().substringAfter("{"))
                    val sessionInfo = nodes[1].text().substring(2, nodes[1].text().indexOf("{")).removePrefix("第").removeSuffix("节").split(",")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo.size
                })
                nodes = nodes.subList(4, nodes.size)
            }
        }
    }
}