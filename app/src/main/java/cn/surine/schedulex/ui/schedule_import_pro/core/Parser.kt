package cn.surine.schedulex.ui.schedule_import_pro.core

import cn.surine.schedulex.ui.schedule_import_pro.core.shell.Normal
import cn.surine.schedulex.ui.schedule_import_pro.data.CourseWrapper
import cn.surine.schedulex.ui.schedule_import_pro.util.ParseData

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 17:03
 */
object Parser {
    fun parse(type: String, html: String, result: (list: List<CourseWrapper>?, e: Exception?) -> Unit) {
        try {
            result(getParse(type).parse(html), null)
        } catch (e: Exception) {
            result(null, e)
        }
    }

    private fun getParse(type: String): IJWParse = ParseData.jwMap[type] ?: Normal()
}