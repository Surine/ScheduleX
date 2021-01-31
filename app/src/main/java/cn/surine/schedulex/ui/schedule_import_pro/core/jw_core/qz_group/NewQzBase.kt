package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.func_parser.TableParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 鉴于新强智的源码的杂乱，现针对其代码进行全面重构
 * 重构思路：
 * 1.源：对于源tag进行参数传递
 * 2.目标：针对于源tag对目标值进行区别实现，不需要设计模式，规模应该没有很大。
 * */

abstract class NewQzBase(val initParam:HashMap<String,String>):TableParser() {
    companion object{
        const val SCHOOL_NAME = "school_name"
        const val TABLE_NAME = "table_name"
        const val KB_CONTENT = "kb_content"
        const val SKIP_ROW = "skip_row"
        const val SKIP_COL = "skip_col"
        const val WINDOW_SIZE = "window_size"
        const val WINDOW_STEP = "window_step"
    }

    override fun getTargetInitFunc(html: String): Element {
        return  Jsoup.parse(html).getElementById(initParam[TABLE_NAME]).getElementsByTag("tbody").first()
    }

    override fun skipRow(): Int = initParam[SKIP_ROW]?.toInt() ?: 0

    override fun skipCol(): Int = initParam[SKIP_COL]?.toInt() ?: 0
}