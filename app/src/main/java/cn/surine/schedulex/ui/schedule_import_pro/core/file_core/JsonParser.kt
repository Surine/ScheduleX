package cn.surine.schedulex.ui.schedule_import_pro.core.file_core

import cn.surine.schedulex.base.utils.Files
import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.base.utils.Toasts.toast
import cn.surine.schedulex.ui.schedule_import_pro.core.IFileParser
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/13 14:32
 */
class JsonParser : IFileParser {
    override fun parse(path: String): List<CourseWrapper>? {
        return try {
            Jsons.parseJsonWithGsonToList(Files.getFileContent(path))
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Json解析出错：" + e.message)
            ArrayList()
        }
    }
}