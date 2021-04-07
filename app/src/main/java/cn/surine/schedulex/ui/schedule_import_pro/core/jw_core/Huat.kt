package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup


//湖北汽车工业学院
class Huat : IJWParse {
    override fun parse(html: String): List<CourseWrapper> {
        val target = Jsoup.parse(html).getElementById("ctl00_ContentPlaceHolder1_CourseTable").child(0) //tbody
        val list = mutableListOf<CourseWrapper>()
        val children = target.children()
        for (i in children.indices) {
            if (i == 0) continue
            val curChild = children[i]
            val curChildChildren = curChild.children()
            for (j in curChildChildren.indices) {
                val curChildChild = curChildChildren[j]
                if (curChildChildren.size == 9 && j <= 1) continue
                if (curChildChildren.size == 8 && j <= 0) continue
                val nodes = curChildChild.text().split(" ")
                println(nodes)
                if (nodes.size < 3) continue
                list.add(CourseWrapper(
                        name = nodes[0], teacher = nodes[1],
                        position = nodes[nodes.lastIndex - 1],week = WeekUtilsV2.parse(nodes[nodes.lastIndex]),
                        day = if(curChildChildren.size == 9) j - 1 else j,
                        sectionStart = i * 2 - 1,
                        sectionContinue = if(i == children.lastIndex) 3 else 2
                ))
            }
        }
        return list
    }
}
