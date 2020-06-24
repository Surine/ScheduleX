package cn.surine.schedulex.ui.timer

import cn.surine.schedulex.base.utils.Dates

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 10:45
 */
object TimerRepository {

    /**
     * 拼接首页Title
     */
    fun getIndexTitle(): String {
        return Dates.getMonthInEng() + Dates.getDate("dd")
    }


    /**
     * 获取今日星期几
     */
    fun getWeekDay(): Int {
        return Dates.getWeekDay()
    }


    /**
     * 获取小部件日视图标题
     */
    fun getWidgetDayClassTitle(): String {
        return Dates.getDate("MM月dd日")
    }
}