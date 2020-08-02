package cn.surine.schedulex.base.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import cn.surine.schedulex.BuildConfig;

/**
 * Intro：日志
 * @author sunliwei
 * @date 2020-01-16 15:54
 */
public class Logs {
    private static boolean isDebug = BuildConfig.DEBUG;
    private static final String LOG_TAG = "slw";


    public static void d(@NonNull String msg){
        if(isDebug){
            Log.d(LOG_TAG, msg);
        }
    }

    public static void e(@NonNull String msg){
        if(isDebug){
            Log.e(LOG_TAG, msg);
        }
    }
}
