package cn.surine.schedulex.base.utils

import android.content.Context
import android.content.SharedPreferences
import cn.surine.schedulex.base.controller.App

/**
 * Intro：
 * Prefs
 * @author sunliwei
 * @date 2020/6/24 11:58
 */

object Prefs {
    val sharedPreferences: SharedPreferences = App.context.getSharedPreferences("data", Context.MODE_PRIVATE)

    /**
     * 保存
     * @param key 键
     * @param value 值
     * */
    @JvmStatic
    fun save(key: String, value: Any?) {
        value ?: return
        sharedPreferences.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
            }
            apply()
        }
    }

    @JvmStatic
    fun getString(key:String,defV:String = "") = sharedPreferences.getString(key,defV)
    @JvmStatic
    fun getInt(key: String,defV: Int) = sharedPreferences.getInt(key,defV)
    @JvmStatic
    fun getBoolean(key: String,defV: Boolean) = sharedPreferences.getBoolean(key,defV)
    @JvmStatic
    fun getLong(key: String,defV: Long) = sharedPreferences.getLong(key,defV)
}