package cn.surine.schedulex.ui.schedule_import_pro.core

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.Normal
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 17:03
 */
object JwParserDispatcher {
    fun parse(type: String, html: String, result: (list: List<CourseWrapper>?, e: Exception?) -> Unit) {
        try {
            result(getParse(type).parse(html), null)
        } catch (e: Exception) {
            result(null, e)
        }
    }

    private fun getParse(type: String): IJWParse = ParseData.jwMap[type] ?: Normal()
}