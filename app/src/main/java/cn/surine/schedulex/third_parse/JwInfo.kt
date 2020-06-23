package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.Constants

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:30
 */
data class JwInfo(val schoolName:String = "", val jwUrl:String = "", val jwType:String = Constants.JW_NORMAL, val author:String = ""){
    companion object{
        const val TUST = "TUST" //科大API
        const val NEW_ZF = "NEW_ZF" //新正方
        const val NEW_URP = "NEW_URP" //新URP
    }
}
