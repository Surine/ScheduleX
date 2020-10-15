package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core

import cn.surine.schedulex.ui.schedule_import_pro.util.ParseUtil

/**
 * Intro：
 * 基于大连工业大学-新强智4
 * @author sunliwei
 * @date 10/15/20 20:41
 */
class NewQz2_1 : NewQz2() {
    //7-13,15(周)
    //7-13,15-17(周)
    //9(周)
    //7-17(周)
    override fun getWeekList(weekInfo: String) =
            when {
                weekInfo.contains(",") -> {
                    val week = weekInfo.substringBefore("(").split(",")
                    val tmp = mutableListOf<Int>()
                    for (w in week) {
                        if (w.contains("-")) {
                            tmp.addAll(ParseUtil.getWeekInfoByStr(w, commonRules = "%d-%d"))
                        } else {
                            tmp.add(w.toInt())
                        }
                    }
                    tmp
                }
                weekInfo.contains("-") -> {
                    ParseUtil.getWeekInfoByStr(weekInfo, commonRules = "%d-%d(周)")
                }
                else -> {
                    listOf(weekInfo.substringBefore("(").toInt())
                }
            }
}