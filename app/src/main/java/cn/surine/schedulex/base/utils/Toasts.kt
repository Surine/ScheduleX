package cn.surine.schedulex.base.utils

import android.widget.Toast
import cn.surine.schedulex.base.controller.App

/**
 * Intro：
 * @author sunliwei
 * @date 2020-01-17 10:50
 */
object Toasts {
    @JvmStatic
    fun toast(s: String?) {
        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun toastLong(s: String?) {
        Toast.makeText(App.context, s, Toast.LENGTH_LONG).show()
    }
}