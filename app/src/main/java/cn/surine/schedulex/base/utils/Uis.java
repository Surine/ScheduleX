package cn.surine.schedulex.base.utils;

import android.content.Context;
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
}
