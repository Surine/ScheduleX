package cn.surine.schedulex.ui.schedule_import_pro.model

import cn.bmob.v3.BmobObject

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 21:01
 */
data class RemoteUniversity(
        val code: String = "",  //学校代号
        var useTimes: Int = 0, //使用次数
        val name: String = "",  //学校名称
        val eng: String = "",  //学校缩写
        val importType: Int = 1, //导入方式  1:jsoup 2.api 3.html -1.其他
        val opInfo: String = "", //操作说明
        val jwUrl: String = "", //教务链接
        val author: String = "", //作者
        val jwSystemName: String = "", //教务系统中文
        val jwSystem: String = "", //教务系统代号
        val status: Int = 0, //0可用 -1不可用
        val version: Int = 0, //对应app版本，低版本无法保证兼容最新适配，所以要有限制
        val forecast: String = "" //教务系统预测
) : BmobObject()