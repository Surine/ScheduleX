package cn.surine.schedulex.base.controller;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.bmob.v3.Bmob;
import cn.surine.schedulex.BuildConfig;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.PrivateInformation;
import cn.surine.ui_lib.SettingItemConfig;

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
        Bugly.init(getApplicationContext(), PrivateInformation.BUGLY_KEY, BuildConfig.DEBUG);
        Bmob.initialize(this, PrivateInformation.BMOB_KEY);
        SettingItemConfig.INSTANCE.setPrimaryColor(getResources().getColor(R.color.colorPrimary));

        //友盟初始化
        UMConfigure.init(this, PrivateInformation.UMENG_KEY, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //初始化组件化基础库, 所有友盟业务SDK都必须调用此初始化接口。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }


}