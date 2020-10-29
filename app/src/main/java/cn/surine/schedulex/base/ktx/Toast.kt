package cn.surine.schedulex.base.ktx

import android.widget.Toast
import cn.surine.schedulex.base.controller.App

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/29/20 11:48
 */
fun toast(s: String?, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, s, time).show()
}
