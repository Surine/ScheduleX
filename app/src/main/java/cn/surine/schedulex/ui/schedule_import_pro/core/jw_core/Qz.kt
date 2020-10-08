package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup

/**
 * Intro：
 * 以湖南工学院首次解析 -旧版强智
 * @author sunliwei
 * @date 10/7/20 22:40
 */

class Qz :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementById("kbtable")
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex < 1 || trIndex == trs.size - 1) continue  //清除表头和表尾
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex < 1) continue  //清除时间列
                val targetDiv = tds[tdIndex].getElementsByTag("div")[1]
                if (targetDiv.textNodes().size == 1) continue
                val courseInfo = targetDiv.text().split(" ")
                courseList.add(CourseWrapper().apply {
                    name = courseInfo[0]
                    teacher = courseInfo[2]
                    position = courseInfo[4]
                    day = tdIndex
                    //session 信息
                    val sessionStr = courseInfo[3].substringAfter("[").substringBefore("节")
                    val sessionInfo = sessionStr.split("-")
                    sectionStart = sessionInfo[0].toInt()
                    sectionContinue = sessionInfo[1].toInt() - sessionInfo[0].toInt() + 1
                    //week 信息
                    val weekInfo = courseInfo[3].substringBefore("周").split(",")
                    val tmpWeek = mutableListOf<Int>()
                    for (i in weekInfo) {
                        if (i.contains("-")) {
                            tmpWeek.addAll(ParseUtil.getWeekInfoByStr(i, commonRules = "%d-%d"))
                        } else {
                            tmpWeek.add(i.toInt())
                        }
                    }
                    week = tmpWeek
                })
            }
        }
        return courseList
    }

}