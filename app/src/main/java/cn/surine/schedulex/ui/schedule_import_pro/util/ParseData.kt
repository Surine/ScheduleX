package cn.surine.schedulex.ui.schedule_import_pro.util

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.*
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group.NewQzBase.Companion.KB_CONTENT
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group.NewQzBase.Companion.SCHOOL_NAME
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group.NewQzBase.Companion.SKIP_ROW
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group.NewQzBase.Companion.TABLE_NAME
import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.qz_group.NewQzV2
import cn.surine.schedulex.ui.schedule_import_pro.model.LocalUniversity

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/21/20 15:22
 */
object ParseData {

    const val tust = "tust" //天津科技大学api

    const val ZF = "zf" //正方
    const val ZF_T6 = "zf_t6" //正方
    const val NEW_ZF = "new_zf" //新正方
    const val QZ = "qz" //强智
    const val NEW_QZ = "new_qz" //新强智
    const val NEW_QZ_2 = "new_qz_2" //新强智2
    const val NEW_QZ_3 = "new_qz_3" //新强智3
    const val NEW_QZ_2_1 = "new_qz_2_1" //新强智2-1
    const val SW = "sw" //树维
    const val NEW_URP = "new_urp" //新urp
    const val JZ = "jz" //金智
    const val CF = "cf"   //乘方

    const val PKU = "pku" //北京大学
    const val NCUT = "ncut" //北方工业大学
    const val SWUST = "swust" //西南科技大学
    const val USTC = "ustc" //中国科学技术大学
    const val WHUT_GRA = "whut_graduates" //武汉理工研究生院
    const val WTU_GRA = "wtu_graduates" //武汉纺织研究生院
    const val HIT_GRA = "hit_graduates" //哈工大研究生院
    const val SANXIA = "sanxia" //三峡
    const val NEW_QZ_2_2 = "new_qz_2_2" //中南林业大学涉外学院
    const val JNU = "jnu" //暨南大学
    const val SICAU = "sicau" //四川农业大学
    const val BUPT = "bupt" //北京邮电大学
    const val ZNLYDXSWXY = "znlydxswxy"  //中南林业大学涉外学院(备用标识符)
    const val DLGYDX = "DLGYDX"  //大连工业大学(备用标识符)
    const val HNLYDX = "HNLYDX"  //华南林业科技大学(备用标识符)

    val jwMap = mapOf(
            ZF to Zf(),
            ZF_T6 to Zf_T6(),
            NEW_ZF to NewZf(),
            SW to Sw(),
            QZ to Qz(),
            NEW_QZ to NewQz(),
            PKU to Pku(),
            NCUT to Ncut(),
            SWUST to Swust(),
            NEW_QZ_2 to NewQz2(),
            NEW_QZ_3 to NewQz3(),
            NEW_QZ_2_1 to NewQz2_1(),
            USTC to Ustc(),
            WHUT_GRA to WhutGraduates(),
            WTU_GRA to WtuGraduates(),
            NEW_URP to NewUrp(),
            JZ to Jz(),
            HIT_GRA to HitGraduates(),
            CF to CF(),
            SANXIA to SanXia(),
            NEW_QZ_2_2 to NewQz2_2(),
            JNU to Jnu(),
            SICAU to Sicau(),
            BUPT to NewQzV2(hashMapOf(
                    SCHOOL_NAME to BUPT,
                    TABLE_NAME to "kbtable",
                    KB_CONTENT to "kbcontent",
                    SKIP_ROW to "1"
            ))
    )

    val commonHtmlSystem = listOf(QZ, NEW_QZ_2_1, SW)
}

class CommonJw {
    val commonJwData = listOf(
            LocalUniversity("正方教务", "-1001", jwSystem = ParseData.ZF),
            LocalUniversity("正方教务-T6", "-1001", jwSystem = ParseData.ZF_T6),
            LocalUniversity("新正方教务", "-1002", jwSystem = ParseData.NEW_ZF),
            LocalUniversity("强智教务", "-1003", jwSystem = ParseData.QZ),
            LocalUniversity("新强智教务", "-1004", jwSystem = ParseData.NEW_QZ),
            LocalUniversity("新强智教务-2", "-10041", jwSystem = ParseData.NEW_QZ_2),
            LocalUniversity("新强智教务-2_1", "-10042", jwSystem = ParseData.NEW_QZ_2_1),
            LocalUniversity("新强智教务-3", "-10043", jwSystem = ParseData.NEW_QZ_3),
            LocalUniversity("树维教务", "-1005", jwSystem = ParseData.SW),
            LocalUniversity("新URP教务", "-1006", jwSystem = ParseData.NEW_URP),
            LocalUniversity("金智教务", "-1007", jwSystem = ParseData.JZ),
            LocalUniversity("乘方教务", "-1008", jwSystem = ParseData.CF)
    )
}