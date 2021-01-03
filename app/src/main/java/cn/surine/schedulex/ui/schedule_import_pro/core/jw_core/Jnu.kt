package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

class Jnu : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val target = Jsoup.parse(html).getElementById("myCourseTable").getElementsByClass("cv-col cv-right")
        val list = mutableListOf<CourseWrapper>()
        for (i in target.indices) {
            val nodes = target[i].getElementsByClass("cv-lesson ")
            for (j in nodes) {
                val item = j.text().split(" ")
                list.add(CourseWrapper().apply {
                    name = item[0]
                    position = item[3]
                    teacher = item[4]
                    day = i + 1
                    week = ParseUtil.getWeekInfoByStr(item[1],singleRules = "%d-%d周(单)",doubleRules = "%d-%d周(双)",commonRules = "%d-%d周")
                    val session = item[2].removeSuffix("节").split("-")
                    sectionStart = session[0].toInt()
                    sectionContinue = session[1].toInt() - session[0].toInt() + 1
                })
            }
        }
        return list
    }
}



