package cn.surine.schedulex.base.utils

/**
 * Intro：
 * 扩展函数
 * @author sunliwei
 * @date 2020/6/23 15:47
 */

/**
 * 列表比特计数
 * 1，3，5 -> 10101
 * 1,2,4 -> 1001
 * */
fun List<Int>.bitCount(maxLen:Int):String{
    val sb = StringBuilder().apply {
        repeat(maxLen){append("0")}
    }
    this.forEach {
        if(it < sb.length){
            sb[it] = '1'
        }
    }
    return sb.toString()
}



