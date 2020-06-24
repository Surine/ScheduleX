package cn.surine.schedulex.data.entity

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:42
 */
class CourseList : BaseVm() {
    //当前周
    @JvmField
    var nowWeek = 0
    //当周课表
    @JvmField
    var courseList: List<Course>? = null
    //共多少周
    @JvmField
    var weeks = 0
}