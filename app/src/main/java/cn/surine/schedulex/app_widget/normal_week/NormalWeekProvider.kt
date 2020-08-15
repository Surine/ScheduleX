package cn.surine.schedulex.app_widget.normal_week

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import cn.surine.schedulex.R
import cn.surine.schedulex.app_widget.core.CoreWidgetProvider
import cn.surine.schedulex.app_widget.core.WidgetUtil
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.utils.Dates
import cn.surine.schedulex.base.utils.Prefs.getBoolean
import cn.surine.schedulex.data.helper.DataHandler

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/2 17:36
 */
class NormalWeekProvider : CoreWidgetProvider() {
    override fun updateUi(context: Context, appWidgetManager: AppWidgetManager, id: Int): RemoteViews {
        val remoteViews = WidgetUtil.getRoot(context, R.layout.widget_normal_week_class)
        //标题配置
        remoteViews.setTextViewText(R.id.widget_day_class_title, if (getBoolean(Constants.NEXT_DAY_STATUS + id, false)) "下周课程" else "本周课程")
        remoteViews.setTextViewText(R.id.widget_day_class_subtitle, if (getBoolean(Constants.NEXT_DAY_STATUS + id, false)) Dates.getDateFormat(Dates.getDateBeforeOfAfter(Dates.getDate(Dates.yyyyMMdd), 7), "MM月dd E") else Dates.getDate("MM月dd E"))
        //配置
        WidgetUtil.setDay(this.javaClass, context, remoteViews, id, R.id.widget_day_class_next_day, intArrayOf(R.drawable.ic_keyboard_arrow_right_black_24dp, R.drawable.ic_keyboard_arrow_left_black_24dp))
        WidgetUtil.setDay(this.javaClass, context, remoteViews, id, R.id.widget_day_class_title)
        WidgetUtil.setDay(this.javaClass, context, remoteViews, id, R.id.widget_day_class_subtitle)
        //显示与隐藏周末
        if (DataHandler.getCurSchedule()?.isShowWeekend == true) {
            WidgetUtil.showOrHide(remoteViews, R.id._sat, View.VISIBLE)
            WidgetUtil.showOrHide(remoteViews, R.id._sun, View.VISIBLE)
        } else {
            WidgetUtil.showOrHide(remoteViews, R.id._sat, View.GONE)
            WidgetUtil.showOrHide(remoteViews, R.id._sun, View.GONE)
        }
        //点击打开主界面
        WidgetUtil.toMain(remoteViews, context, R.id.root)
        WidgetUtil.addList(NormalWeekService::class.java, context, appWidgetManager, remoteViews, R.id.listview, id)
        return remoteViews
    }
}