package cn.surine.schedulex.base.controller;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.surine.schedulex.BuildConfig;
import cn.surine.schedulex.R;

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
        Beta.upgradeDialogLayoutId = R.layout.view_upgrade_dialog;
        Bugly.init(getApplicationContext(), "2d69e03a71", BuildConfig.DEBUG);

        Bmob.initialize(this, "dacda5b9e1bf28816c68b55c80813f83");
    }


}