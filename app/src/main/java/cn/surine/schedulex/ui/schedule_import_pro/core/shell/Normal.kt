package cn.surine.schedulex.ui.schedule_import_pro.core.shell

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse
import cn.surine.schedulex.ui.schedule_import_pro.data.CourseWrapper


/**
 * Intro：
 *  默认解析器，不提供任何功能
 * @author sunliwei
 * @date 9/21/20 17:11
 */
class Normal : IJWParse {
    override fun parse(html: String): List<CourseWrapper> = emptyList()
}