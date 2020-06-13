package cn.surine.schedulex.app_widget.core;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import cn.surine.schedulex.app_widget.data.Actions;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.ui.MainActivity;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/30 20:41
 */
public class WidgetUtil {


    /**
     * 获取界面视图
     */
    public static RemoteViews getRoot(Context context, int layoutId) {
        return new RemoteViews(context.getPackageName(), layoutId);
    }


    /**
     * 下一天
     */
    public static void nextDay() {

    }


    public static void preDay() {

    }

    /**
     * 打开App
     */
    public static void toMain(RemoteViews remoteViews, Context context, int id) {
        Intent intent = new Intent(context, MainActivity.class);
        String str = Constants.APP_WIDGET_ID;
        remoteViews.setOnClickPendingIntent(id, PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * 添加列表
     */
    public static void addList(Class cls, Context context, AppWidgetManager appWidgetManager, RemoteViews remoteViews, int listId, int appId) {
        Intent intent = new Intent(context, cls);
        context.stopService(intent);
        intent.putExtra(Constants.APP_WIDGET_ID, appId);
        remoteViews.setRemoteAdapter(listId, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appId, listId);
    }

    /**
     * 配置next按钮事假
     */
    static int cur = 0;

    public static void setDay(Class target, Context context, RemoteViews remoteViews, int appId, int layoutId, int[] icons) {
        if (icons == null || icons.length < 2)
            throw new IllegalArgumentException("icons length is not match, must be two");
        boolean isNextDay = Prefs.getBoolean(Constants.NEXT_DAY_STATUS + appId, false);
        Intent intent = new Intent(context, target);
        if (!isNextDay) {
            remoteViews.setImageViewResource(layoutId, icons[0]);
            intent.setAction(Actions.NEXT_DAY);
        } else {
            remoteViews.setImageViewResource(layoutId, icons[1]);
            intent.setAction(Actions.PREVIOUS_DAY);
        }
        intent.putExtra(Constants.APP_WIDGET_ID, appId);
        remoteViews.setOnClickPendingIntent(layoutId, PendingIntent.getBroadcast(context, cur++, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }


    public static void setDay(Class target, Context context, RemoteViews remoteViews, int appId, int layoutId) {
        boolean isNextDay = Prefs.getBoolean(Constants.NEXT_DAY_STATUS + appId, false);
        Intent intent = new Intent(context, target);
        if (!isNextDay) {
            intent.setAction(Actions.NEXT_DAY);
        } else {
            intent.setAction(Actions.PREVIOUS_DAY);
        }
        intent.putExtra(Constants.APP_WIDGET_ID, appId);
        remoteViews.setOnClickPendingIntent(layoutId, PendingIntent.getBroadcast(context, cur++, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
