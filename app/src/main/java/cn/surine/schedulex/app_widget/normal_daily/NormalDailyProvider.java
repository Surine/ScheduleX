package cn.surine.schedulex.app_widget.normal_daily;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.core.CoreWidgetProvider;
import cn.surine.schedulex.app_widget.core.WidgetUtil;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.Prefs;

import static cn.surine.schedulex.base.utils.Dates.getDate;
import static cn.surine.schedulex.base.utils.Dates.getDateBeforeOfAfter;
import static cn.surine.schedulex.base.utils.Dates.getDateFormat;
import static cn.surine.schedulex.base.utils.Dates.yyyyMMdd;

public class NormalDailyProvider extends CoreWidgetProvider {

    //这里更新UI
    public RemoteViews updateUi(Context context, AppWidgetManager appWidgetManager, int appId) {
        RemoteViews remoteViews = WidgetUtil.getRoot(context, R.layout.widget_normal_daily_class);
        //标题配置
        remoteViews.setTextViewText(R.id.widget_day_class_title, Prefs.getBoolean(Constants.NEXT_DAY_STATUS + appId, false) ? "明日课程" : "今日课程");
        remoteViews.setTextViewText(R.id.widget_day_class_subtitle, Prefs.getBoolean(Constants.NEXT_DAY_STATUS + appId, false) ? getDateFormat(getDateBeforeOfAfter(getDate(yyyyMMdd), 1), "MM月dd E") : getDate("MM月dd E"));
        //配置今日明日
        WidgetUtil.setDay(this.getClass(), context, remoteViews, appId, R.id.widget_day_class_next_day, new int[]{R.drawable.ic_keyboard_arrow_right_black_24dp, R.drawable.ic_keyboard_arrow_left_black_24dp});
        WidgetUtil.setDay(this.getClass(), context, remoteViews, appId, R.id.widget_day_class_title);
        WidgetUtil.setDay(this.getClass(), context, remoteViews, appId, R.id.widget_day_class_subtitle);
        //点击打开主界面
        WidgetUtil.toMain(remoteViews, context, R.id.root);
        //配置列表
        WidgetUtil.addList(NormalDailyService.class, context, appWidgetManager, remoteViews, R.id.listview, appId);
        return remoteViews;
    }
}
