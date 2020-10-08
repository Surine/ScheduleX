package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.Jsoup

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 22:34
 */
class Ncut :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = arrayListOf<CourseWrapper>()
        val element = Jsoup.parse(html).getElementById("Table6")
        //表格行
        val trs = element.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            //第一行为表头
            if (trIndex == 0) continue
            //每一行的列
            val tr = trs[trIndex]
            val tds = tr.getElementsByTag("td")
            for (tdIndex in tds.indices) {
                if (tdIndex == 0) continue
                val td = tds[tdIndex]
                val p = td.getElementsByTag("p").first()
                //跳过空白p标签
                if (p.html() == """&nbsp;""") continue
                //p标签中会包含多个a标签
                for (aInfo in p.getElementsByTag("a")) {
                    val courseInfo = aInfo.textNodes()
                    val weekInfo = aInfo.getElementsByTag("font").textNodes()[1].text()
                    val modifyWeekInfo = weekInfo.substring(1, weekInfo.length - 1)
                    val courseWrapper = CourseWrapper()
                    courseWrapper.name = courseInfo[0].text()
                    courseWrapper.position = courseInfo[1].text()
                    courseWrapper.teacher = courseInfo[2].text()
                    courseWrapper.day = tdIndex
                    courseWrapper.sectionStart = trIndex * 2 - 1
                    courseWrapper.sectionContinue = 2
                    val startWeek = modifyWeekInfo.substringBefore("-")
                    val endWeek = modifyWeekInfo.substringAfter("-")
                    val weekResult = when {
                        endWeek.contains("单") -> {
                            (startWeek.toInt()..endWeek.removeSuffix("单周").toInt()).toList().filter { it % 2 == 1 }
                        }
                        endWeek.contains("双") -> {
                            (startWeek.toInt()..endWeek.removeSuffix("双周").toInt()).toList().filter { it % 2 == 0 }
                        }
                        else -> {
                            (startWeek.toInt()..endWeek.removeSuffix("周").toInt()).toList()
                        }
                    }
                    courseWrapper.week = weekResult
                    courseList.add(courseWrapper)
                }
            }
        }
        return courseList
    }

}