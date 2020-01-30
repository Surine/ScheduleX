package cn.surine.schedulex.base.utils;

import android.widget.Toast;

import cn.surine.schedulex.base.controller.App;

/**
 * Intro：
 * @author sunliwei
 * @date 2020-01-17 10:50
 */
public class Toasts {
    public static void toast(String s){
        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show();
    }
}
