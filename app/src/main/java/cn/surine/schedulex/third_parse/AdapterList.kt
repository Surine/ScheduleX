package cn.surine.schedulex.third_parse

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:33
 */
object AdapterList {

    /**
     * 学校列表
     * from jsonArray
     * */
    val schoolList = mutableListOf<JwInfo>().apply {
//        add(JwInfo("天津科技大学", jwType = JwInfo.TUST, author = "Surine"))
//        add(JwInfo("武汉纺织大学", "http://ehall.wtu.edu.cn/new/index.html", "newZenFang", "花生酱啊"))
    }

    /**
     * 教务系统
     * */
    val systemlist = mutableListOf<JwInfo>().apply {
        add(JwInfo(school = "新正方教务", author = "花生酱啊"))
//        add(JwInfo(schoolName = "新URP教务", author = "Surine"))
    }
}