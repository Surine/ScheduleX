package cn.surine.schedulex.third_parse.new_version

/**
 * schedulex解析器通用适配实体类
 * */

class CourseWrapper {
    /*课程名*/
    var name = ""

    /*上课地点*/
    var position = ""

    /*老师*/
    var teacher = ""

    /*周几上,取值1-7*/
    var day = 1

    /*第几节开始 取值1 - x*/
    var sectionStart = 0

    /*持续几节，注意是持续的节次，比如说两小节，这个值就为2*/
    var sectionContinue = 1

    /*哪几周上课，如[1,3,4,6,7,8]表示1，3，4，6，7，8周上课*/
    var week: List<Int> = ArrayList()

    override fun toString(): String =
            "name=$name\nposition=$position\nteacher=$teacher\nday=$day\n" +
                    "sectionStart=$sectionStart\nsectionContinue=$sectionContinue\nweek=$week"

}