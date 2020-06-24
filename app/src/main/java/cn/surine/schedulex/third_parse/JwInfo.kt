package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.data.entity.BaseVm

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:30
 */
data class JwInfo(val school:String = "",val name:String = "", val url:String = "", val system:String = Constants.JW_NORMAL, val author:String = ""):BaseVm(){
    companion object{
        const val TUST = "tust" //科大API
        const val NEW_ZF = "NEW_ZF" //新正方
        const val NEW_URP = "NEW_URP" //新URP
    }
}
