package cn.surine.schedulex.base.utils

/**
 * Intro：
 * 扩展函数
 * @author sunliwei
 * @date 7/17/20 15:04
 */

/**
 * 对0 - 9 补前置0
 * */
fun Int.supplyZero(): String = if (this < 10) "0$this" else this.toString()