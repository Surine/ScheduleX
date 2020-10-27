package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.nodes.Element

/**
 * Intro：
 * 基于江苏大学京江学院
 * @author sunliwei
 * @date 10/27/20 15:17
 */
class Zf_T6 : Zf() {

    override fun targetId(): String {
        return "Table6"
    }

    override fun parseInfo(element: Element, courseList: MutableList<CourseWrapper>, trIndex: Int, tdIndex: Int, size: Int) {
        var nodes = element.textNodes()
        if (nodes.size % 4 == 0) {
            while (nodes.size != 0) {
                courseList.add(CourseWrapper().apply {
                    name = nodes[0].text()
                    position = nodes[3].text()
                    teacher = nodes[2].text()
                    day = if (size == 9) {
                        tdIndex - 1
                    } else {
                        tdIndex
                    }
                    week = ParseUtil.getWeekInfoByStr(nodes[1].text().substringAfter("/"), singleRules = "单周(%d-%d)",doubleRules = "双周(%d-%d)",commonRules = "周(%d-%d)")
                    sectionStart = trIndex - 1
                    sectionContinue = nodes[1].text().substringBefore("节").toInt()
                })
                nodes = nodes.subList(4, nodes.size)
            }
        }
    }
}