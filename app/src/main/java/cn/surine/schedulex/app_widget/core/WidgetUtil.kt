package cn.surine.schedulex.app_widget.core

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import cn.surine.schedulex.app_widget.data.Actions
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.Prefs.getBoolean
import cn.surine.schedulex.ui.MainActivity

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/30 20:41
 */
object WidgetUtil {
    /**
     * 获取界面视图
     */
    fun getRoot(context: Context, layoutId: Int): RemoteViews {
        return RemoteViews(context.packageName, layoutId)
    }

    /**
     * 打开App
     */
    fun toMain(remoteViews: RemoteViews, context: Context?, id: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val str = Constants.APP_WIDGET_ID
        remoteViews.setOnClickPendingIntent(id, PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
    }

    /**
     * 添加列表
     */
    fun addList(cls: Class<*>?, context: Context, appWidgetManager: AppWidgetManager, remoteViews: RemoteViews, listId: Int, appId: Int) {
        val intent = Intent(context, cls)
        context.stopService(intent)
        intent.putExtra(Constants.APP_WIDGET_ID, appId)
        remoteViews.setRemoteAdapter(listId, intent)
        appWidgetManager.notifyAppWidgetViewDataChanged(appId, listId)
    }

    /**
     * 配置next按钮事假
     */
    private var cur = 0
    fun setDay(target: Class<*>?, context: Context?, remoteViews: RemoteViews, appId: Int, layoutId: Int, icons: IntArray?) {
        require(!(icons == null || icons.size < 2)) { "icons length is not match, must be two" }
        val isNextDay = getBoolean(Constants.NEXT_DAY_STATUS + appId, false)
        val intent = Intent(context, target)
        if (!isNextDay) {
            remoteViews.setImageViewResource(layoutId, icons[0])
            intent.action = Actions.NEXT_DAY
        } else {
            remoteViews.setImageViewResource(layoutId, icons[1])
            intent.action = Actions.PREVIOUS_DAY
        }
        intent.putExtra(Constants.APP_WIDGET_ID, appId)
        remoteViews.setOnClickPendingIntent(layoutId, PendingIntent.getBroadcast(context, cur++, intent, PendingIntent.FLAG_UPDATE_CURRENT))
    }

    fun setDay(target: Class<*>?, context: Context?, remoteViews: RemoteViews, appId: Int, layoutId: Int) {
        val isNextDay = getBoolean(Constants.NEXT_DAY_STATUS + appId, false)
        val intent = Intent(context, target)
        if (!isNextDay) {
            intent.action = Actions.NEXT_DAY
        } else {
            intent.action = Actions.PREVIOUS_DAY
        }
        intent.putExtra(Constants.APP_WIDGET_ID, appId)
        remoteViews.setOnClickPendingIntent(layoutId, PendingIntent.getBroadcast(context, cur++, intent, PendingIntent.FLAG_UPDATE_CURRENT))
    }

    fun showOrHide(remoteViews: RemoteViews, dayView: Int, status: Int) {
        remoteViews.setViewVisibility(dayView, status)
    }
}