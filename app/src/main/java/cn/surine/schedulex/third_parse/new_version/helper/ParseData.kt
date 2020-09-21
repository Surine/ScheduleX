package cn.surine.schedulex.third_parse.new_version.helper

import cn.surine.schedulex.third_parse.JwInfo
import cn.surine.schedulex.third_parse.new_version.parses.Zf

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 15:22
 */
object ParseData {
    val jwMap = mapOf(
            "zf" to Zf()  //正方
    )

    val systemlist = mutableListOf<JwInfo>().apply {
        add(JwInfo(school = "新正方教务", author = "花生酱啊", system = JwInfo.NEW_ZF))
        add(JwInfo(school = "正方教务", author = "Surine", system = JwInfo.ZF))
//        add(JwInfo(schoolName = "新URP教务", author = "Surine"))
    }
}