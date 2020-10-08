package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.Jsoup

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 22:39
 */
class Swust :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val target = Jsoup.parse(html).getElementsByClass("UICourseTable").first()
        val tbody = target.getElementsByTag("tbody").first()
        val trs = tbody.getElementsByTag("tr")
        for (i in trs.indices) {
            val tr = trs[i]
            val tds = tr.getElementsByTag("td")
            for (j in tds.indices) {
                val td = tds[j]
                if (td.text().isEmpty() || arrayOf("上午", "下午", "晚上").contains(td.text()) ||
                        (td.text().startsWith("第") && td.text().endsWith("讲"))) {
                    continue
                }
                val div = td.getElementsByClass("lecture").first()
                courseList.add(CourseWrapper().apply {
                    name = div.getElementsByClass("course").text()
                    teacher = div.getElementsByClass("teacher").text()
                    position = div.getElementsByClass("place").text()
                    sectionContinue = 2
                    day = if (tds.size == 9) j - 1 else j
                    sectionStart = (i + 1) * 2 - 1
                    val weekText = div.getElementsByClass("week").text()
                    week = if (weekText.contains("(1)")) {
                        val d1 = weekText.substringBefore("(").split("-").map { it.toInt() }
                        (d1[0]..d1[1]).filterIndexed { index, _ -> index % 2 == 0 }
                    } else {
                        val d1 = weekText.substringBefore("(").split("-").map { it.toInt() }
                        (d1[0]..d1[1]).toList()
                    }
                })
            }
        }
        return courseList
    }

}