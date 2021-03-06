package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 常州大学
 * */
class Czuu :TableParser(){
    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element?:return emptyList()
        val courseList = mutableListOf<CourseWrapper>()
        val datas = element.text().split(" ").toMutableList()
        if(datas.size <= 1)return emptyList()
        if(datas.size == 2){
            //网课
            datas.add(2,"/"); //添加一个虚拟地址
        }
        if(datas.size == 4){
            //包含单双周
            datas[2] += datas.removeAt(3)
        }
        courseList.add(CourseWrapper().apply {
            name = datas[0]
            position = datas[1]
            week = WeekUtilsV2.parse(datas[2])
            sectionStart = trIndex
            sectionContinue = 1
            day = tdIndex
            teacher = ""
        })
        return courseList
    }

    override fun getTargetInitFunc(html: String): Element {
        return Jsoup.parse(html).getElementById("GVxkkb")
    }

    override fun skipRow() = 1

    override fun skipCol() = 1

}
