package cn.surine.schedulex.third_parse.new_version.parses

import cn.surine.schedulex.third_parse.new_version.bean.CourseWrapper

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 15:26
 */
interface IParse {
    fun parse(html: String):List<CourseWrapper>
}