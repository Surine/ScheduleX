package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.beans

//{
//    "kcmc":"理论力学",
//    "kcbh":"122123",
//    "jxbmc":"2018级物理学1班",
//    "kcrwdm":"1090811",
//    "jcdm2":"03,04",
//    "zcs":"16,9,10,11,12,13,14,15",
//    "xq":"4",
//    "jxcdmcs":"1号教学楼504",
//    "teaxms":"孔"
//},
data class ZhaoQingUBean(
        val kcmc:String = "", //课程名
        val teaxms:String = "", //教师
        val jxcdmcs:String = "", //上课地点
        val xq:String = "", //星期
        val zcs:String = "", //周信息
        val jcdm2:String = "" //节次
)