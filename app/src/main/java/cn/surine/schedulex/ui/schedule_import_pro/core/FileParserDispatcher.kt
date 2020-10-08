package cn.surine.schedulex.ui.schedule_import_pro.core

import cn.surine.schedulex.ui.schedule_import_pro.core.file_core.CsvParser
import cn.surine.schedulex.ui.schedule_import_pro.core.file_core.ExcelParser
import cn.surine.schedulex.ui.schedule_import_pro.core.file_core.JsonParser

/**
 * Intro：
 * 文件解析
 *
 * @author sunliwei
 * @date 2020/5/13 14:32
 */
object FileParserDispatcher {
    const val JSON = "json"
    const val EXCEL_XLS = "xls"
    const val EXCEL_XLSX = "xlsx"
    const val CSV = "csv"

    operator fun get(suffix: String?): IFileParser {
        return when (suffix) {
            JSON -> JsonParser()
            EXCEL_XLS, EXCEL_XLSX -> ExcelParser()
            CSV -> CsvParser()
            else -> JsonParser()
        }
    }
}