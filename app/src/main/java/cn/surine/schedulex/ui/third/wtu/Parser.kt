package cn.surine.schedulex.ui.third.wtu

class Parser {
    fun parse(engine:(String)->String,html:String):String{
        return try {
            engine(html)
        }catch (e:Exception){
            e.localizedMessage?:"未知错误"
        }
    }
}