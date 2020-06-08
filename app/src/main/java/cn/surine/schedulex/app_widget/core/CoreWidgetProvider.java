package cn.surine.schedulex.app_widget.core;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import cn.surine.schedulex.app_widget.data.Actions;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.Prefs;

public abstract class CoreWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, this.getClass()));
        for (int appWidgetId : appWidgetIds) {
            //自动切换
            if (Actions.NEXT_DAY.equals(intent.getAction())) {
                Prefs.save(Constants.NEXT_DAY_STATUS + appWidgetId, true);
            } else if (Actions.PREVIOUS_DAY.equals(intent.getAction())) {
                Prefs.save(Constants.NEXT_DAY_STATUS + appWidgetId, false);
            }
            am.updateAppWidget(appWidgetId, updateUi(context, am, appWidgetId));
        }
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int i, Bundle bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle);
        //这里外界可能需要数据
        appWidgetManager.updateAppWidget(i, updateUi(context, appWidgetManager, i, bundle));
    }

    @Override
    public void onDeleted(Context context, int[] iArr) {
        super.onDeleted(context, iArr);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        for (int id : iArr) {
            appWidgetManager.updateAppWidget(id, updateUi(context, appWidgetManager, id));
        }
    }


    /**
     * 将所有与界面相关的操作都放在这里
     */
    protected abstract RemoteViews updateUi(Context context, AppWidgetManager appWidgetManager, int id);
    //如果需要重写，就重写。
    protected RemoteViews updateUi(Context context, AppWidgetManager appWidgetManager, int id, Bundle bundle) {
        return updateUi(context, appWidgetManager, id);
    }

}
