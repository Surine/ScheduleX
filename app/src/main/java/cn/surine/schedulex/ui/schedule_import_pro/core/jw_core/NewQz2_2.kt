package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil


//base 中南林业大学涉外学院
class NewQz2_2 : NewQz2(){
    override fun getWeekList(weekInfo: String): List<Int> {
        val week = weekInfo.substringAfter(")").substringBefore("[").trim()
        return ParseUtil.getWeekInfoByStr(week,singleRules = "%d-%d(单周)",doubleRules = "%d-%d(双周)",commonRules = "%d-%d(全部)")
    }
}