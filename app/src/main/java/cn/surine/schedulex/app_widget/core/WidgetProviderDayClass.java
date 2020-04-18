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

import static cn.surine.schedulex.base.utils.Dates.getDate;
import static cn.surine.schedulex.base.utils.Dates.getDateBeforeOfAfter;
import static cn.surine.schedulex.base.utils.Dates.getDateFormat;
import static cn.surine.schedulex.base.utils.Dates.yyyyMMdd;

public class WidgetProviderDayClass extends AppWidgetProvider {
    private static int CURRENT;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, WidgetProviderDayClass.class));
        for (int i = 0; i < appWidgetIds.length; i++) {
            //自动切换
            //如果是

            if (Actions.NEXT_DAY.equals(intent.getAction())) {
                Prefs.save(Constants.NEXT_DAY_STATUS + appWidgetIds[i], true);
            } else if (Actions.PREVIOUS_DAY.equals(intent.getAction())) {
                Prefs.save(Constants.NEXT_DAY_STATUS + appWidgetIds[i], false);
            }
            updateUi(context, am, appWidgetIds[i]);
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
        remoteViews.setTextViewText(R.id.widget_day_class_subtitle, Prefs.getBoolean(sb.toString(), false) ? getDateFormat(getDateBeforeOfAfter(getDate(yyyyMMdd), 1), "MM月dd E") : getDate("MM月dd E"));
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
        boolean isNextDay = Prefs.getBoolean(Constants.NEXT_DAY_STATUS + i, false);
        Intent intent = new Intent(context, WidgetProviderDayClass.class);
        if (!isNextDay) {
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
