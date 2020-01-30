package cn.surine.schedulex.base.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StatusBars {

    /**
     * 隐藏状态栏
     * @param activity 待处理Acitivity
     * */
    public static void hideStatusbar(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 设置透明状态栏
     * @param activity 页面
     * */
    public static void fullScreenStatusBar(Activity activity) {
        setStatusBarColor(activity, Color.TRANSPARENT);
    }


    /**
     * 设置状态栏颜色
     * @param activity 待处理activity
     * @param color 状态栏颜色
     * */
    public static void setStatusBarColor(Activity activity,int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(color);
        }
    }


    /**
     * 设置状态栏文字颜色（Google原生）
     * @param activity UI
     * @param dark 是否黑色（true黑色，false白色）
     * */
    public static void setStatusBarTextColor(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 对外接口,配置状态栏
     * @param activity activity
     * @param dark 通知栏文字颜色是否为黑色，true为黑色
     * */
    public static void setStatusBarUI(Activity activity, boolean dark){
        //下方方法调用顺序不能改变
        StatusBars.fullScreenStatusBar(activity);
        StatusBars.setStatusBarTextColor(activity,dark);
    }


}