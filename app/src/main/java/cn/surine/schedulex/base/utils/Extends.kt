package cn.surine.schedulex.base.utils

import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.base.controller.BaseAdapter

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
        if (it < sb.length) {
            sb[it] = '1'
        }
    }
    return sb.toString()
}

/**
 * RecyclerView加载器
 * @param layoutManager 布局
 * @param adapter 适配器
 * */
fun <T> RecyclerView.load(mLayoutManager: RecyclerView.LayoutManager, mAdapter: BaseAdapter<T>, adapterOp:(adapter:BaseAdapter<T>)->Unit){
    layoutManager = mLayoutManager
    adapter = mAdapter
    adapterOp(mAdapter)
}



