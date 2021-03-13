package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * 四川农业大学
 * */
class Sicau : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val courseList = mutableListOf<CourseWrapper>()
        val t = Jsoup.parse(html).getElementsByTag("table")
        val target = if(t.size == 1) t[0] else t[t.lastIndex - 1]
        val trs = target.getElementsByTag("tr")
        for (trIndex in trs.indices) {
            if (trIndex <= 1) continue
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                //去除无效行
                if (tds.size == 8 && tdIndex == 0) continue
                if (tds.size == 9 && tdIndex <= 1) continue
                val nodes = tds[tdIndex].textNodes()
//                Logs.d(nodes.toString())
                if (nodes.size <= 1) continue
                var lastIndex = 0
                val cacheList = mutableListOf<List<TextNode>>()
                for (nIndex in nodes.indices) {
                    //节次分割
                    if (nodes[nIndex].text().contains("-----")) {
                        try {
                            cacheList.add(nodes.subList(lastIndex, nIndex))
                        } catch (e: Exception) {

                        }
                        lastIndex = nIndex + 1
                    }
                }
                cacheList.forEachIndexed { index, it ->
                    courseList.add(CourseWrapper().apply {
                        name = it[0].text().split("：")[0]
                        teacher = it[0].text().split("：")[1]
                        position = it[1].toString()
                        day = if (tds.size == 8) tdIndex else (tdIndex - 1)
                        sectionStart = 2 * (trIndex - 1) - 1
                        sectionContinue = 2
                        week = getWeek(index, tds[tdIndex], it[2])
                    })
                }

            }
        }
        return courseList
    }

    private fun getWeek(index: Int, element: Element?, textNode: TextNode): List<Int> {
        element ?: return emptyList()
        val curElement: Element = try {
            element.getElementsByTag("font").filter {
                it.text().contains("周")
            }[index]
        }catch (e:Exception){
            Element("null")
        }

        val targetStr = textNode.text().substringBefore("(")
        if (targetStr.contains("-")) {
            return when {
                curElement.text().contains("单") -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周").filter { it % 2 == 1 }
                }
                curElement.text().contains("双") -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周").filter { it % 2 == 0 }
                }
                else -> {
                    ParseUtil.getWeekInfoByStr(targetStr, commonRules = "%d-%d周")
                }
            }
        } else {
            return targetStr.removeSuffix("周").split(",").map {
                it.toInt()
            }.toList()
        }
    }
}