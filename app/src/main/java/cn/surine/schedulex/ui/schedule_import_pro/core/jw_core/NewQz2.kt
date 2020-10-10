package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import android.util.Log
import cn.surine.schedulex.base.utils.Files
import cn.surine.schedulex.base.utils.Jsons
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
class NewQz2 : IJWParse {

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
                courseList.add(CourseWrapper().apply {
                    name = div.textNodes()[0].text()
                    teacher = infos[0].text()
                    position = if (infos.size == 3) infos[2].text() else ""
                    sectionStart = trIndex * 2 - 1
                    sectionContinue = 2
                    day = tdIndex + 1
                    val weekInfo = infos[1].text()
                    week = if (weekInfo.contains("-")) {
                        //常规解析
                        ParseUtil.getWeekInfoByStr(weekInfo, commonRules = "%d-%d(周)")
                    } else {
                        //非常规解析
                        weekInfo.substringBefore("(").split(",").map {
                            it.toInt()
                        }
                    }
                })
            }
        }
        return courseList
    }
}