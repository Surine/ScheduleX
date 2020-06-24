package cn.surine.schedulex.app_widget.core

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import cn.surine.schedulex.app_widget.data.Actions
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.Prefs.save

abstract class CoreWidgetProvider : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val am = AppWidgetManager.getInstance(context)
        val appWidgetIds = am.getAppWidgetIds(ComponentName(context, this.javaClass))
        for (appWidgetId in appWidgetIds) { //自动切换
            if (Actions.NEXT_DAY == intent.action) {
                save(Constants.NEXT_DAY_STATUS + appWidgetId, true)
            } else if (Actions.PREVIOUS_DAY == intent.action) {
                save(Constants.NEXT_DAY_STATUS + appWidgetId, false)
            }
            am.updateAppWidget(appWidgetId, updateUi(context, am, appWidgetId))
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, i: Int, bundle: Bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle)
        //这里外界可能需要数据
        appWidgetManager.updateAppWidget(i, updateUi(context, appWidgetManager, i, bundle))
    }

    override fun onDeleted(context: Context, iArr: IntArray) {
        super.onDeleted(context, iArr)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, iArr: IntArray) {
        super.onUpdate(context, appWidgetManager, iArr)
        for (id in iArr) {
            appWidgetManager.updateAppWidget(id, updateUi(context, appWidgetManager, id))
        }
    }

    /**
     * 将所有与界面相关的操作都放在这里
     */
    protected abstract fun updateUi(context: Context, appWidgetManager: AppWidgetManager, id: Int): RemoteViews

    //如果需要重写，就重写。
    protected fun updateUi(context: Context, appWidgetManager: AppWidgetManager, id: Int, bundle: Bundle?): RemoteViews {
        return updateUi(context, appWidgetManager, id)
    }
}