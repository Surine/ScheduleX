package cn.surine.schedulex.third_parse

import android.util.Log
import cn.surine.schedulex.third_parse.Shell.NewZFParser
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
    fun default(html: String):List<CourseWrapper>? = null

    /**
     * 新正方系统解析器
     */
    fun newZenFang(html: String): List<CourseWrapper> = wrap(::NewZFParser,html)

    /**
     * 其他教务系统解析器
     * 也不知道啥学校用的啥系统，知道了就把对应的解析器复制过来
     * 替换wrap(::NewZFParser,html)里面的NewZFParser就应该ok
     */
}