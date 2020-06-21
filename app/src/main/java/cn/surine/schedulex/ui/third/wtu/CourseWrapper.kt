package cn.surine.schedulex.ui.third.wtu

class CourseWrapper {
    /*课程名*/
    private val name = ""

    /*上课地点*/
    private val position = ""

    /*老师*/
    private val teacher = ""

    /*周几上,取值1-7*/
    private val day = 1

    /*第几节开始 取值1 - x*/
    private val sectionStart = 0

    /*持续几节，注意是持续的节次，比如说两小节，这个值就为2*/
    private val sectionContinue = 1

    /*哪几周上课，如[1,3,4,6,7,8]表示1，3，4，6，7，8周上课*/
    private val week: List<Int> = ArrayList()
}