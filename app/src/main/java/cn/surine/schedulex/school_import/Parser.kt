package cn.surine.schedulex.school_import

class Parser {
    fun parse(engine:(String)->String,html:String):String{
        return try {
            engine(html)
        }catch (e:Exception){
            e.localizedMessage?:"未知错误"
        }
    }
}