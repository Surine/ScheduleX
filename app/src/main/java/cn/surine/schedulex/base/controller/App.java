package cn.surine.schedulex.base.controller;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import cn.surine.schedulex.BuildConfig;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-17 10:37
 */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), "2d69e03a71", BuildConfig.DEBUG);
    }

}
