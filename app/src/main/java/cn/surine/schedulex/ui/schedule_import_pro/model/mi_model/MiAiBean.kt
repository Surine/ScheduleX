package cn.surine.schedulex.ui.schedule_import_pro.model.mi_model

/**
 * Intro：
 * 小米课程表实体类
 * @author sunliwei
 * @date 9/6/20 23:15
 */
data class MiAiBean(
    val code: Int,
    val desc: String,
    val data: MiAiCourseInfo
)

data class MiAiCourseInfo(
    //当前周
    val current: Int,
    var courses: MutableList<CourseInfo>
)

data class CourseInfo(
    //课程名
    val name: String,
    //位置
    val position: String,
    //老师
    val teacher: String,
    //周
    val weeks: String,
    //星期x
    val day: Int,
    //节次
    var sections: String,
) {
    var tag: Boolean = false
}

data class Section(
    var section: Int
)