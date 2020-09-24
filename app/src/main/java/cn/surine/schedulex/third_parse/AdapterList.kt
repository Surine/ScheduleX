package cn.surine.schedulex.third_parse

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:33
 */
object AdapterList {

    /**
     * 教务系统
     * */
    val systemlist = mutableListOf<JwInfo>().apply {
        add(JwInfo(school = "新正方教务", author = "花生酱啊",system = JwInfo.NEW_ZF))
        add(JwInfo(school = "正方教务", author = "Surine",system = JwInfo.ZF))
        add(JwInfo(school = "树维教务", author = "Surine",system = JwInfo.SW))
//        add(JwInfo(schoolName = "新URP教务", author = "Surine"))
    }
}