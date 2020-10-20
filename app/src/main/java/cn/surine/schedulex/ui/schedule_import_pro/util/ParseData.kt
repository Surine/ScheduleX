package cn.surine.schedulex.ui.schedule_import_pro.util

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.*
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
    const val NEW_ZF = "new_zf" //新正方
    const val QZ = "qz" //强智
    const val NEW_QZ = "new_qz" //新强智
    const val NEW_QZ_2 = "new_qz_2" //新强智2
    const val NEW_QZ_3 = "new_qz_3" //新强智3
    const val NEW_QZ_2_1 = "new_qz_2_1" //新强智2-1
    const val SW = "sw" //树维

    const val PKU = "pku" //北京大学
    const val NCUT = "ncut" //北方工业大学
    const val SWUST = "swust" //西南科技大学
    const val USTC = "ustc" //中国科学技术大学
    const val WHUT_GRA = "whut_graduates" //武汉理工研究生院

    val jwMap = mapOf(
            ZF to Zf(),
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
            WHUT_GRA to WhutGraduates()
    )

    val commonHtmlSystem = listOf(QZ, NEW_QZ_2_1,SW)

    val commonJwData = listOf(
            LocalUniversity("正方教务", "-1001", jwSystem = ZF),
            LocalUniversity("新正方教务", "-1002", jwSystem = NEW_ZF),
            LocalUniversity("强智教务", "-1003", jwSystem = QZ),
            LocalUniversity("新强智教务", "-1004", jwSystem = NEW_QZ),
            LocalUniversity("新强智教务-2", "-10041", jwSystem = NEW_QZ_2),
            LocalUniversity("新强智教务-2_1", "-10042", jwSystem = NEW_QZ_2_1),
            LocalUniversity("新强智教务-3", "-10043", jwSystem = NEW_QZ_3),
            LocalUniversity("树维教务", "-1005", jwSystem = SW)
    )
}