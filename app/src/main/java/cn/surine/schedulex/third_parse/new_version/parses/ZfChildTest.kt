package cn.surine.schedulex.third_parse.new_version.parses

import cn.surine.schedulex.third_parse.new_version.bean.CourseWrapper
import org.jsoup.nodes.Element

/**
 * Intro：
 * 通用系统子类
 * @author sunliwei
 * @date 9/21/20 16:26
 */
class ZfChildTest:Zf(){
    /**
     * 直接操作解析部分即可，可以省略
     * */
    override fun parseInfo(element: Element, courseList: MutableList<CourseWrapper>) {
        super.parseInfo(element, courseList)
    }
}