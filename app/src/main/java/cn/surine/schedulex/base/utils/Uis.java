package cn.surine.schedulex.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Intro：
 * ui工具类
 *
 * @author sunliwei
 * @date 2020-02-03 11:03
 */
public class Uis {

    /**
     * 获取反射布局
     *
     * @param context 上下文
     * @param id      layoutId
     */
    public static View inflate(Context context, int id) {
        return LayoutInflater.from(context).inflate(id, null);
    }


    /**
     * 是否是暗黑模式
     * */
    public static boolean getDarkModeStatus(Context context) {
        int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}