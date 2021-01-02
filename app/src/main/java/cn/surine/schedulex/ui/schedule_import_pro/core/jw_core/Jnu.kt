package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import org.jsoup.Jsoup

class Jnu :IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val target = Jsoup.parse(html).getElementById("myCourseTable")
        return emptyList()
    }

}