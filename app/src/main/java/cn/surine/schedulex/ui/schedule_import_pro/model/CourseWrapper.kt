package cn.surine.schedulex.ui.schedule_import_pro.model

/**
 * schedulex解析器通用适配实体类
 * */

data class CourseWrapper(
    /*课程名*/
    var name: String = "",

    /*上课地点*/
    var position: String = "",

    /*老师*/
    var teacher: String = "",

    /*周几上,取值1-7*/
    var day: Int = 1,

    /*第几节开始 取值1 - x*/
    var sectionStart: Int = 0,

    /*持续几节，注意是持续的节次，比如说两小节，这个值就为2*/
    var sectionContinue: Int = 1,

    /*哪几周上课，如[1,3,4,6,7,8]表示1，3，4，6，7，8周上课*/
    var week: List<Int> = ArrayList()
)