package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group

import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData.BUPT
import cn.surine.schedulex.ui.schedule_import_pro.util.WeekUtilsV2
import org.jsoup.nodes.Element

class NewQzV2(initParam: HashMap<String, String>) : NewQzBase(initParam) {

    val backList = mutableListOf<CourseWrapper>()

    override fun itemParse(element: Element?, trIndex: Int, tdIndex: Int): List<CourseWrapper> {
        element ?: return emptyList()
        val div = element.getElementsByClass(initParam[KB_CONTENT]).first() ?: return emptyList()
        val infos = div.getElementsByTag("font")
        if (infos == null || infos.isEmpty()) return emptyList()
        val courseList = mutableListOf<CourseWrapper>()
        infos.windowed(initParam[WINDOW_SIZE]?.toInt() ?: 3, initParam[WINDOW_STEP]?.toInt()
                ?: 3).forEachIndexed { index, it ->
            val courseWrapper = CourseWrapper().apply {
                name = getCourseName(initParam, div, index, it)
                teacher = getTeacher(initParam, div, it)
                position = getPosition(initParam, div, it)
                sectionStart = getSectionStart(initParam, div, it, trIndex)
                sectionContinue = getContinueSession(initParam, div, it, trIndex)
                day = getCourseDay(initParam, div, it, tdIndex)
                week = getWeekInfo(initParam, div, it)
            }
            if (!backList.contains(courseWrapper)) {
                backList.add(courseWrapper)
                courseList.add(courseWrapper)
            }
        }
        return courseList
    }

    private fun getWeekInfo(initParam: HashMap<String, String>, div: Element, it: List<Element>): List<Int> {
        return WeekUtilsV2.parse(it[1].text())
    }

    private fun getCourseDay(initParam: HashMap<String, String>, div: Element, it: List<Element>, tdIndex: Int): Int {
        return tdIndex + 1
    }

    private fun getContinueSession(initParam: HashMap<String, String>, div: Element, it: List<Element>, trIndex: Int): Int {
        return when (initParam[SCHOOL_NAME]) {
            BUPT -> {
                //北邮：[06-07-08节]   [08-09节]
                val text = it[1].text().substringAfter("[").substringBefore("]").split("-")
                text.size
            }
            else -> {
                //华南林业科技 固定2节
                //大连工业大学 固定2节
                //中南林业大学涉外学院 固定2节
                2
            }
        }
    }

    private fun getSectionStart(initParam: HashMap<String, String>, div: Element, it: List<Element>, trIndex: Int): Int {
        return when (initParam[SCHOOL_NAME]) {
            BUPT -> {
                //北邮：[06-07-08节]   [08-09节]
                val text = it[1].text().substringAfter("[").substringBefore("]").split("-")
                text[0].toInt()
            }
            else -> {
                //华南林业科技 表格计数
                //大连工业大学 表格计数
                //中南林业大学涉外学院 表格计数
                trIndex * 2 - 1
            }
        }
    }

    private fun getPosition(initParam: HashMap<String, String>, div: Element, it: List<Element>): String {
        return if (it.size == 3) it[2].text() else ""
    }

    private fun getTeacher(initParam: HashMap<String, String>, div: Element, it: List<Element>): String {
        return it[0].text()
    }

    private fun getCourseName(initParam: HashMap<String, String>, div: Element, index: Int, it: List<Element>): String {
        return div.textNodes()[2 * index].text()
    }
}