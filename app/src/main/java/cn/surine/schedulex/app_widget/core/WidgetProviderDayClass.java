package cn.surine.schedulex.app_widget.core;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.data.Actions;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.ui.MainActivity;
public class WidgetProviderDayClass extends AppWidgetProvider {
    private static int CURRENT;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, WidgetProviderDayClass.class));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateUi(context,am,appWidgetIds[i]);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        for (int updateUi : iArr) {
            updateUi(context, appWidgetManager, updateUi);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int i, Bundle bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle);
        updateUi(context, appWidgetManager, i);
    }

    public void updateUi(Context context, AppWidgetManager appWidgetManager, int appId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_day_class);
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.NEXT_DAY_STATUS);
        sb.append(appId);
        remoteViews.setTextViewText(R.id.widget_day_class_title, Prefs.getBoolean(sb.toString(), false) ? "明日课程" : "今日课程");
        configNextDayCourse(remoteViews, appId, context);
        Intent intent = new Intent(context, MainActivity.class);
        String str = Constants.APP_WIDGET_ID;
        intent.putExtra(str, appId);
        int i2 = CURRENT;
        CURRENT = i2 + 1;
        remoteViews.setOnClickPendingIntent(R.id.widget_day_class_title, PendingIntent.getActivity(context, i2, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Intent intent2 = new Intent(context, WidgetServiceDayClass.class);
        intent2.putExtra(str, appId);
        remoteViews.setRemoteAdapter(R.id.listview, intent2);
        appWidgetManager.updateAppWidget(appId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appId, R.id.listview);
    }

    private void configNextDayCourse(RemoteViews remoteViews, int i, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.NEXT_DAY_STATUS);
        sb.append(i);
        boolean z = Prefs.getBoolean(sb.toString(), false);
        Intent intent = new Intent(context, WidgetProviderDayClass.class);
        if (!z) {
            remoteViews.setImageViewResource(R.id.widget_day_class_next_day, R.drawable.ic_keyboard_arrow_right_black_24dp);
            intent.setAction(Actions.NEXT_DAY);
        } else {
            remoteViews.setImageViewResource(R.id.widget_day_class_next_day, R.drawable.ic_keyboard_arrow_left_black_24dp);
            intent.setAction(Actions.PREVIOUS_DAY);
        }
        intent.putExtra(Constants.APP_WIDGET_ID, i);
        int i2 = CURRENT;
        CURRENT = i2 + 1;
        remoteViews.setOnClickPendingIntent(R.id.widget_day_class_next_day, PendingIntent.getBroadcast(context, i2, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
