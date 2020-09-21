package cn.surine.schedulex.third_parse.new_version.core

import cn.surine.schedulex.third_parse.new_version.bean.CourseWrapper
import cn.surine.schedulex.third_parse.new_version.helper.ParseData
import cn.surine.schedulex.third_parse.new_version.parses.IParse
import cn.surine.schedulex.third_parse.new_version.parses.Normal

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 17:03
 */
object Parser {
    fun parse(type: String, html: String, result: (list: List<CourseWrapper>?, e: Exception?) -> Unit) {
        try {
            result(getIparse(type).parse(html), null)
        } catch (e: Exception) {
            result(null, e)
        }
    }

    private fun getIparse(type: String): IParse = ParseData.jwMap[type] ?: Normal()
}