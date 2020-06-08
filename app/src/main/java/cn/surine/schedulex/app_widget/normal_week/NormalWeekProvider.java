package cn.surine.schedulex.app_widget.normal_week;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.core.CoreWidgetProvider;
import cn.surine.schedulex.app_widget.core.WidgetUtil;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/2 17:36
 */
public class NormalWeekProvider extends CoreWidgetProvider {
    @Override
    protected RemoteViews updateUi(Context context, AppWidgetManager appWidgetManager, int id) {
        RemoteViews remoteViews = WidgetUtil.getRoot(context, R.layout.widget_normal_week_class);
        WidgetUtil.addList(NormalWeekService.class,context,appWidgetManager,remoteViews,R.id.listview,id);
        return remoteViews;
    }
}
