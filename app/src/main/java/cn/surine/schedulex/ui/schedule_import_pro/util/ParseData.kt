package cn.surine.schedulex.ui.schedule_import_pro.util

import cn.surine.schedulex.ui.schedule_import_pro.core.jw_core.*

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
    const val SW = "sw" //树维

    const val PKU = "pku" //北京大学
    const val NCUT = "ncut" //北方工业大学
    const val SWUST = "swust" //西南科技大学

    val jwMap = mapOf(
            ZF to Zf(),
            NEW_ZF to NewZf(),
            SW to Sw(),
            QZ to Qz(),
            NEW_QZ to NewQz(),
            PKU to Pku(),
            NCUT to Ncut(),
            SWUST to Swust(),
            NEW_QZ_2 to NewQz2()
    )

}