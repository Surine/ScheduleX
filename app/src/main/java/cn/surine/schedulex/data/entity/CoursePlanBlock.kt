package cn.surine.schedulex.data.entity

import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil

/**
 * 添加课程的安排数据块
 * */
data class CoursePlanBlock(
        val belongId:String = "",  //所属课程
        @Transient var expand: Boolean = true, // 是否展开
        var weeks: List<Int> = listOf(),  //上课周
        var day: Int = 1,  //上课日
        var sessionStart: Int = 1,  //课程开始节次
        var sessionEnd: Int = 1,  //课程结束节次
        var teacher: String = "无教师",   //老师
        var position: String = "无地点",  //位置
        var score: String = "无学分"  //学分
) {
    //获取星期信息
    fun getDayStr() = ParseUtil.weekList[day - 1]

    //获取节次信息
    fun getSessionStr() = "上课节次：$sessionStart - $sessionEnd 节"

    //获取周信息
    fun getWeekStr() = if (weeks.isEmpty()) "暂无上课周" else "上课周：$weeks"

    fun getPlanTimeInfo() = "$weeks ${ParseUtil.weekList[day - 1]} $sessionStart-$sessionEnd 节"
    fun getPlanExtraInfo() = "教师：$teacher 位置：$position 学分：$score"

    fun isWarning() = weeks.isEmpty()
}