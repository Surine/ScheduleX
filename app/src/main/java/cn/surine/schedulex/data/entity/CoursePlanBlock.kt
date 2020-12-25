package cn.surine.schedulex.data.entity

/**
 * 添加课程的安排数据块
 * */
data class CoursePlanBlock(
    var extra:String = "",  //额外数据
    var weeks:List<Int> = listOf(),  //上课周
    var day:Int = 1,  //上课日
    var sessionStart:Int = 1,  //课程开始节次
    var sessionEnd:Int = 1,  //课程结束节次
    var teacher:String = "",   //老师
    var position:String = "",  //位置
    var score:String = ""  //学分
)