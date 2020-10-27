package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import android.util.Log
import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * 正方教务-基于浙江大学宁波理工学院
 * @author sunliwei
 * @date 9/21/20 15:26
 */
open class Zf() : IJWParse {

    open fun targetId() = "Table1"

    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById(targetId())
        //表格行
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex <= 1) continue  //清除表头（包括早晨一行）
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                parseInfo(tds[tdIndex], courseList, trIndex, tdIndex, tds.size)
            }
        }
        return courseList
    }

    override fun parseInfo(element: Element, courseList: MutableList<CourseWrapper>, trIndex: Int, tdIndex: Int, size: Int) {
        var nodes = element.textNodes()
        if (nodes.size % 4 == 0) {
            while (nodes.size != 0) {
                courseList.add(CourseWrapper().apply {
                    name = nodes[0].text()
                    position = nodes[3].text()
                    teacher = nodes[2].text()
                    day = ParseUtil.getDayInfoByStr(nodes[1].text().substring(0, 2))
                    week = ParseUtil.getWeekInfoByStr(nodes[1].text().substringAfter("{"),
                            singleRules = "第%d-%d周|单周}",
                            doubleRules = "第%d-%d周|双周}",
                            commonRules = "第%d-%d周}"
                    )
                    val sessionInfo = nodes[1].text().substring(2, nodes[1].text().indexOf("{")).removePrefix("第").removeSuffix("节").split(",")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo.size
                })
                nodes = nodes.subList(4, nodes.size)
            }
        }
    }
}