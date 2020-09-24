package cn.surine.schedulex.third_parse

import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.data.entity.BaseVm

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/22 11:30
 */
data class JwInfo(val school:String = "",val name:String = "", val url:String = "", val system:String = Constants.JW_NORMAL, val author:String = "",val version:Int = 0):BaseVm(){
    companion object{
        const val TUST = "tust" //科大API
        const val NEW_ZF = "newZenFang" //新正方
        const val PKU = "pku" //北京大学
        const val NEW_URP = "newUrp" //新URP
        const val NORMAL = "normal" //默认
        const val NCUT = "ncut" //北方工业
        const val ZF = "zf" //正方
        const val SW = "sw" //树维
    }
}
