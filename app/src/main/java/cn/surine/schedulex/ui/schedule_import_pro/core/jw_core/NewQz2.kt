package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

/**
 * Intro：
 * 基于华南林业科技大学-新强智2
 * @author sunliwei
 * @date 10/10/20 20:24
 */
open class NewQz2 : IJWParse {

    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("kbtable").getElementsByTag("tbody").first()
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex == 0) continue  //跳过首行
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                val div = tds[tdIndex].getElementsByClass("kbcontent").first() ?: continue
                val infos = div.getElementsByTag("font")
                if (infos == null || infos.isEmpty()) continue
                //适用于单格子多课程的
                infos.windowed(3,3).forEachIndexed { index,it->
                    courseList.add(CourseWrapper().apply {
                        name = div.textNodes()[2 * index].text()
                        teacher = it[0].text()
                        position = if (it.size == 3) it[2].text() else ""
                        sectionStart = trIndex * 2 - 1
                        sectionContinue = 2
                        day = tdIndex + 1
                        val weekInfo = it[1].text()
                        week = getWeekList(weekInfo)
                    })
                }
            }
        }
        return courseList
    }

    open fun getWeekList(weekInfo: String): List<Int> {
        return if (weekInfo.contains("-")) {
            //常规解析
            ParseUtil.getWeekInfoByStr(weekInfo, commonRules = "%d-%d(周)")
        } else {
            //非常规解析
            weekInfo.substringBefore("(").split(",").map {
                it.toInt()
            }
        }
    }
}