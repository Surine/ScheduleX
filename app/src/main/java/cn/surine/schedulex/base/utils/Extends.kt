package cn.surine.schedulex.base.utils

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.R
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.controller.BaseAdapter
import cn.surine.schedulex.ui.view.custom.helper.CommonDialogs

/**
 * Intro：
 * 扩展函数
 * @author sunliwei
 * @date 2020/6/23 15:47
 */

/**
 * List
 * 列表比特计数
 * 1，3，5 -> 10101
 * 1,2,4 -> 1001
 * @param maxLen 最大长度
 * */
fun List<Int>.bitCount(maxLen: Int): String {
    val sb = StringBuilder().apply {
        repeat(maxLen) { append("0") }
    }
    this.forEach {
        if (it <= sb.length) {
            sb[it - 1] = '1'
        }
    }
    return sb.toString()
}

/**
 * RecyclerView加载器
 * @param layoutManager 布局
 * @param adapter 适配器
 * */
fun <T> RecyclerView.load(mLayoutManager: RecyclerView.LayoutManager, mAdapter: BaseAdapter<T>, adapterOp: (adapter: BaseAdapter<T>) -> Unit) {
    layoutManager = mLayoutManager
    adapter = mAdapter
    adapterOp(mAdapter)
}


fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Fragment.alert(s: String) {
    this.context?.let { CommonDialogs.getCommonDialog(it, "警告", s).show() }
}

/**
 * 对0 - 9 补前置0
 * */
fun Int.supplyZero(): String = if (this < 10) "0$this" else this.toString()


/**对视图进行分类管理*/

//用于渲染数据
inline fun Fragment.ui(block: () -> Unit) {
    block()
}

//用于处理监听
inline fun Fragment.click(block: () -> Unit) {
    block()
}

//用于初始化
inline fun Fragment.init(block: () -> Unit) {
    block()
}

//用于数据驱动
inline fun Fragment.dataDriver(block: () -> Unit) {
    block()
}

//此生仅进行一次的操作
fun once(key: String, block: () -> Unit) {
    if (!Prefs.getBoolean(key, false)) {
        block()
        Prefs.save(key, true)
    }
}

//解析布局
fun Int.parseUi():View{
    return LayoutInflater.from(App.context).inflate(this, null)
}