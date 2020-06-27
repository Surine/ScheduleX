package cn.surine.schedulex.third_parse

class Parser {
    fun parse(engine: (String) -> List<CourseWrapper>?, html: String, result: (list: List<CourseWrapper>?,e:Exception?) -> Unit) {
        try {
            result(engine(html),null)
        } catch (e: Exception) {
           result(null,e)
        }
    }
}