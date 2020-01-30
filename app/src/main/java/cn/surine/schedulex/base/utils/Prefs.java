package cn.surine.schedulex.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.surine.schedulex.base.controller.App;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-17 16:37
 */
public class Prefs {
    private static SharedPreferences sharedPreferences = App.context.getSharedPreferences("data", Context.MODE_PRIVATE);

    /**
     * 存储
     *
     * @param key 键
     * @param obj 值
     */
    public static void save(String key, Object obj) {
        SharedPreferences.Editor editor = App.context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        if (obj instanceof String) {
            editor.putString(key, String.valueOf(obj));
        }

        if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        }

        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        }

        if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        }

        if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        }

        editor.apply();
    }


    public static String getString(String key, String defV) {
        return sharedPreferences.getString(key, defV);
    }


    public static int getInt(String key, int defV) {
        return sharedPreferences.getInt(key, defV);
    }


    public static boolean getBoolean(String key, boolean defV) {
        return sharedPreferences.getBoolean(key, defV);
    }

    public static Long getLong(String key,Long defV){
        return sharedPreferences.getLong(key,defV);
    }
}
