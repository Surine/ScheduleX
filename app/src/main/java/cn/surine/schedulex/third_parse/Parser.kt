package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.utils.Toasts.toast
import com.tencent.bugly.crashreport.CrashReport

class Parser {
    fun parse(engine: (String) -> List<CourseWrapper>?, html: String, result: (list: List<CourseWrapper>?) -> Unit) {
        toast("开始解析，请稍后~")
        try {
            result(engine(html))
        } catch (e: Exception) {
            CrashReport.postCatchedException(RuntimeException(
                    "[${e.localizedMessage}] : $html"
            ))
            toast("导入失败：${e.localizedMessage}")
        }
    }
}