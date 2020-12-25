package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.beans.ZhaoQingUBean
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper

/**
 * 肇庆学院教务处解析
 * */
class ZhaoQingU:IJWParse{
    override fun parse(html: String): List<CourseWrapper> {
        val after = html.substringAfter("var kbxx =")
        val before = after.substringBefore("""${'$'}.each(kbxx""".trimMargin())
        val endStr = before.trim().dropLast(1)
        val uBeans:List<ZhaoQingUBean>? = Jsons.parseJsonWithGsonToList(endStr)
        uBeans?:return emptyList()
        val res = mutableListOf<CourseWrapper>()
        for (i in uBeans){
            val weeks = i.zcs.split(",").map {
                it.toInt()
            }.sorted()
            val session = i.jcdm2.split(",").map {
                it.toInt()
            }
            res.add(CourseWrapper(
               name = i.kcmc,teacher = i.teaxms,
                    position = i.jxcdmcs,
                    day = i.xq.toInt(),
                    week = weeks,
                    sectionStart = session[0],
                    sectionContinue = session.size
            ))
        }
        return res
    }
}
