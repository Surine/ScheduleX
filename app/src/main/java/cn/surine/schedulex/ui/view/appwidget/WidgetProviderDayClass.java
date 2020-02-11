package cn.surine.schedulex.ui.view.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import cn.surine.schedulex.BuildConfig;
import cn.surine.schedulex.R;
import cn.surine.schedulex.ui.MainActivity;

/**
 * Intro：
 * 小部件
 * @author sunliwei
 * @date 2019-08-14 10:32
 */
public class WidgetProviderDayClass extends AppWidgetProvider {
    int i;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_day_class);
        //设置数据
        Intent serviceIntent = new Intent(context, WidgetServiceDayClass.class);
        intent.setData(Uri.fromParts("content", "" + i++, null));
        remoteView.setRemoteAdapter(R.id.listview, serviceIntent);

        //更新
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, WidgetProviderDayClass.class));
        am.updateAppWidget(appWidgetIds, remoteView);
        am.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listview);

    }

}
