package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Intro：
 * 金智(base 南京工业大学浦江学院)
 * @author sunliwei
 * @date 11/2/20 16:51
 */

class Jz : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val target = Jsoup.parse(html).getElementsByClass("CourseFormTable").first().getElementsByTag("tbody").first()
        val trs = target.getElementsByTag("tr")
        //需要重新构建一个完整的二维数组
        val gridList = Array<Array<Element?>>(trs.size + 1) { Array(7) { null } }
        //首位索引
        var firstIndex = 0
        for (trIndex in trs.indices) {
            //跳过首行周信息
            if (trIndex == 0) continue
            val tds = trs[trIndex].getElementsByTag("td")
            for (tdIndex in tds.indices) {
                //跳过首列节与隐藏
                if (tdIndex <= 1) continue
                val curTd = tds[tdIndex]
                //计算此元素占据的行
                var occupyRow = 0
                try {
                    occupyRow = curTd.attr("rowspan").toInt()
                } catch (e: Exception) {
                    break
                }
                var filled = false
                //只有填充完成才结束
                while (!filled) {
                    val row = firstIndex / 7
                    val col = firstIndex % 7
                    //将下方格子都填满
                    var i = occupyRow
                    while (i > 0) {
                        val cur = gridList[row + (--i)][col]
                        if (cur == null) {
                            gridList[row + i][col] = curTd
                            filled = true
                        }
                    }
                    firstIndex++
                }
            }
        }


        val list = mutableListOf<CourseWrapper>()
        for (i in gridList.indices) {
            for (j in gridList[i].indices) {
                gridList[i][j] ?: continue
                val nodes = gridList[i][j]?.textNodes()
                if (nodes == null || nodes.size == 0 || nodes.size == 1) continue
                val nodesMutableList = nodes.toMutableList()
                nodesMutableList.removeAt(0)
                val handleNode = nodesMutableList.windowed(3, 3)
                handleNode.map {
                    val info1 = it[0].text().trim().split(" ")
                    val sessionInfo = info1[2].substringAfter("第").substringBefore("节").split("-")
                    //如果有多个，只添加真正为本格子的那个
                    if (sessionInfo[0].toInt() == i + 1) {
                        list.add(CourseWrapper().apply {
                            position = it[2].text()
                            teacher = it[1].text()
                            name = info1[0]
                            week = ParseUtil.getWeekInfoByStr(info1[1].substringBefore(" "), singleRules = "%d-%d周(单)",doubleRules = "%d-%d周(双)",commonRules = "%d-%d周")
                            sectionStart = sessionInfo[0].toInt()
                            sectionContinue = sessionInfo[1].toInt() - sectionStart + 1
                            day = j + 1
                        })
                    }
                }
            }
        }
        return list
    }
}
