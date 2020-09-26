package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.utils.Toasts
import cn.surine.schedulex.third_parse.Shell.NewZFParser
import cn.surine.schedulex.third_parse.Shell.maintaining
import cn.surine.schedulex.third_parse.Shell.pku
import cn.surine.schedulex.third_parse.bean.Course

object ParserEngine {

    /**
     * 转化器
     */
    private fun converter(course:Course):CourseWrapper{
        val courseWrapper = CourseWrapper()
        courseWrapper.name = course.name
        courseWrapper.position = course.room
        courseWrapper.teacher = course.teacher
        courseWrapper.day = course.day
        courseWrapper.sectionStart = course.startNode
        courseWrapper.sectionContinue = course.endNode - course.startNode + 1
        for (i in course.startWeek..course.endWeek) {
            when (course.type) {
                0 -> courseWrapper.week = courseWrapper.week.plus(i)
                1 -> if (i % 2 == 1) courseWrapper.week = courseWrapper.week.plus(i)
                2 -> if (i % 2 == 0) courseWrapper.week = courseWrapper.week.plus(i)
            }
        }
        return courseWrapper
    }

    /**
     * WakeUp解析器一层封装
     */
    private fun wrap(kernel:(String)->List<Course>, html: String):List<CourseWrapper>{
        val resultList: List<Course> = kernel(html)
        val data = mutableListOf<CourseWrapper>()
        for (course in resultList)
            data.add(converter(course = course))
        return data
    }

    /**
     * 默认解析器
     */
    fun default(html: String):List<CourseWrapper>?{
        Toasts.toast("提交课表网页,我们100%适配")
        return null
    }

    /**
     * 新正方系统解析器
     *      武汉纺织大学、华南理工大学
     */
    fun newZenFang(html: String): List<CourseWrapper> = wrap(::NewZFParser,html)

    /**
     * 北京大学系统解析器
     */
    fun PKU(html: String): List<CourseWrapper> = wrap(::pku,html)

    /**
     * 华中科技大学系统解析器
     */
    fun HUST(html: String): List<CourseWrapper> = wrap(::maintaining,html)

    /**
     * 武汉大学系统解析器
     */
    fun WHU(html: String): List<CourseWrapper> = wrap(::maintaining,html)

    /**
     * 北方工业大学
     * */
    fun NCUT(html: String): List<CourseWrapper> = Shell.ncut(html)

    /**
     * 正方教务
     * */
    fun ZF(html: String):List<CourseWrapper> = Shell.zf(html)

    /**
     * 树维教务
     * */
    fun SW(html: String):List<CourseWrapper> = Shell.sw(html)

    /**旧版强智*/
    fun OLD_QZ(html: String):List<CourseWrapper> = Shell.old_qz(html)
}