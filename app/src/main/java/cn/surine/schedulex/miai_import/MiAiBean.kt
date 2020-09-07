package cn.surine.schedulex.miai_import

/**
 * Intro：
 * 小米课程表实体类
 * @author sunliwei
 * @date 9/6/20 23:15
 */
data class MiAiBean(
        val status:Int,
        val data:MiAiCourseInfo
)

data class MiAiCourseInfo(
        //当前周
        val presentWeek: Int,
        //总共周
        val totalWeek: Int,
        val courseInfos: List<CourseInfo>
)

data class CourseInfo(
        //课程名
        val name: String,
        //位置
        val position: String,
        //老师
        val teacher: String,
        //周
        val weeks: List<Int>,
        //星期x
        val day: Int,
        //节次
        val sections: List<Section>
)

data class Section(
        val section: Int
)