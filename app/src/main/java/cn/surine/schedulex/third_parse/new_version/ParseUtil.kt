package cn.surine.schedulex.third_parse.new_version

import android.util.Log
import cn.surine.schedulex.base.Constants

/**
 * Intro：
 *  parse util
 * @author sunliwei
 * @date 9/21/20 10:46
 */
object ParseUtil {
    val commonMap = mapOf("周一" to 1, "周二" to 2, "周三" to 3, "周四" to 4, "周五" to 5, "周六" to 6, "周日" to 7)

    /**
     * 根据字符串返回目标
     * @param target 字符串
     * @param baseData 基础模板
     * */
    fun getDayInfoByStr(target: String, baseData: Map<String, Int> = commonMap) = commonMap[target] ?: 1

    /**
     * 根据模板来返回解析出来的周信息
     * @param rules 字符串模板(str数组，需要按照单，双，全周来传递模板)
     * @param target 待解析字符串
     * */
    fun getWeekInfoByStr(rules: Array<String>, target: String): List<Int> {
        if (rules.size != 3) return emptyList()
        when {
            '单' in target -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(rules[0],i,j) == target) {
                            return (i..j).toList().filter { it % 2 != 0 }
                        }
                    }
                }
            }
            '双' in target -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(rules[1],i,j) == target) {
                            return (i..j).toList().filter { it % 2 == 0 }
                        }
                    }
                }
            }
            else -> {
                for (i in 1 until Constants.MAX_SESSION) {
                    for (j in i until Constants.MAX_SESSION) {
                        if (String.format(rules[2],i,j) == target) {
                            return (i..j).toList()
                        }
                    }
                }
            }
        }
        return emptyList()
    }
}