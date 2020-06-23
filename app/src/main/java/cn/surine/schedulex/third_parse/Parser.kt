package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.utils.Toasts.toast
import com.tencent.bugly.crashreport.CrashReport

class Parser {
    fun parse(engine:(String)->List<CourseWrapper>?,html:String):List<CourseWrapper>?{
        return try {
            engine(html)
        }catch (e:Exception){
            CrashReport.postCatchedException(RuntimeException(
                    "[${e.localizedMessage}] : $html"
            ))
            toast("导入失败：${e.localizedMessage}")
            null
        }
    }
}