package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * HIT Graduate Parser
 * @author sunliwei
 * @date 11/13/20 10:57
 */
class HitGraduates : TableParser() {
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        val list = mutableListOf<CourseWrapper>()
        element ?: return emptyList()
        val nodes = element.text()
        if (nodes.isEmpty()) return emptyList()
        val nodesList = nodes.split(ParseUtil.SESSION)
        for (no in nodesList) {
            if (no.isEmpty()) continue
            val sp = no.split("◇").toMutableList()
            if (sp.size != 3) {
                //说明没分割地点
                sp.add(sp[1].substringAfter(ParseUtil.WEEK).removePrefix("]"))
                sp[1] = sp[1].removeSuffix(sp[2])
            }
            val weeks = sp[1].split(ParseUtil.WEEK)
            for (week in weeks) {
                if (week == "[" || week == "]") continue
                var weekTmp = week
                if (week.startsWith("]")) {
                    weekTmp = week.removeSuffix("]，")
                }
                list.add(CourseWrapper().apply {
                    name = sp[0].trim()
                    position = sp[2].substringBefore("[").trim()
                    day = tdIndex - 1
                    teacher = weekTmp.substringBefore("[").trim().substringAfter("，")
                    val session = sp[2].substringAfter("[").substringBefore("]").split("，")
                    sectionStart = session[0].trim().toInt()
                    sectionContinue = if (session.size == 1) 1 else session[1].trim().removeSuffix("]").toInt() - sectionStart + 1
                    val weekInfo = weekTmp.substringAfter("[")
                    this.week = when {
                        weekInfo.contains("-") -> {
                            ParseUtil.getWeekInfoByStr(weekInfo, commonRules = "%d-%d")
                        }
                        weekInfo.contains("，") -> {
                            weekInfo.split("，").map { it.trim().toInt() }
                        }
                        else -> {
                            listOf(weekInfo.trim().toInt())
                        }
                    }
                })
            }
        }
        return list
    }

    override fun getTargetInitFunc(html: String): Element = Jsoup.parse(html).getElementById("xszp").getElementsByTag("tbody").first()

    override fun skipRow() = 1

    override fun skipCol() = 2

}
