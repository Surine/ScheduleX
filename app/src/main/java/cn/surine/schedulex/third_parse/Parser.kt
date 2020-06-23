package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.utils.Toasts.toast
import com.tencent.bugly.crashreport.CrashReport

class Parser {
    fun parse(type: String, html: String): List<CourseWrapper>? {
        return try {
            when (type) {
                JwInfo.NEW_ZF -> ParserEngine.newZenFang(html)
                else -> null
            }
        } catch (e: Exception) {
            CrashReport.postCatchedException(RuntimeException(
                    "[${e.localizedMessage}] : $html"
            ))
            toast("导入失败：${e.localizedMessage}")
            null
        }
    }


}