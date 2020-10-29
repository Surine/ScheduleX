package cn.surine.schedulex.base.ktx

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import cn.surine.schedulex.base.controller.App

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/29/20 11:50
 */

private val sp: SharedPreferences = App.context.getSharedPreferences("data", Context.MODE_PRIVATE)

/**
 * save data
 * */
fun save(commit: Boolean = true,
         action: SharedPreferences.Editor.() -> Unit) {
    sp.edit(commit) {
        action()
    }
}

/**
 * read data
 * */
fun <T> read(key: String, def: T) {
    when (def) {
        is Int -> sp.getInt(key, def)
        is Float -> sp.getFloat(key, def)
        is String -> sp.getString(key, def)
        is Boolean -> sp.getBoolean(key, def)
        is Long -> sp.getLong(key, def)
    }
}
