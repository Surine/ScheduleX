package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 河南农业大学解析器
 * */
class Henau : TableParser(){
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element?:return emptyList()
        val courseList = mutableListOf<CourseWrapper>()
        if(element.hasClass("td1")){
            return emptyList()
        }
        val divNodes = element.getElementsByTag("div")[0]
        if (divNodes.childrenSize() == 0){
            return emptyList()
        }
        val courseName = divNodes.child(0).text()
        val textNodes = divNodes.textNodes()
        val sectionInfo = textNodes[1].text().substringAfter("[").substringBefore("]").split("-")
        val course = CourseWrapper().apply {
            name = courseName
            teacher = textNodes[0].text()
            position = textNodes[2].text()
            week = WeekUtilsV2.parse(textNodes[1].text().substringBeforeLast("["))
            sectionStart = sectionInfo[0].toInt()
            sectionContinue = sectionInfo[1].toInt() - sectionInfo[0].toInt() + 1
            day = if(element.parent().childrenSize() == 9) tdIndex - 1 else tdIndex
        }
        return courseList.apply {
            add(course)
        }
    }

    override fun getTargetInitFunc(html: String) = Jsoup.parse(html).getElementById("mytable")!!

    override fun skipRow() = 1

    override fun skipCol() = 1

}
