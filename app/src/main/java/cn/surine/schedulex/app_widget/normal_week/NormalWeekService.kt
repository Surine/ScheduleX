package cn.surine.schedulex.app_widget.normal_week

import android.content.Context
import android.widget.RemoteViews
import cn.surine.coursetableview.entity.BTimeTable
import cn.surine.schedulex.R
import cn.surine.schedulex.app_widget.core.WeekRemoteViewService
import cn.surine.schedulex.app_widget.normal_week.BitmapMaker.createWeekBitmap
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.interfaces.DCall
import cn.surine.schedulex.base.utils.Prefs.getBoolean
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.helper.DataHandler

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/8 15:08
 */
class NormalWeekService : WeekRemoteViewService() {
    private var maxSession = 0
    override fun dataSetChanged(mAppWidgetId: Int, courseCall: DCall<List<Course>>, bTimeTableDCall: DCall<BTimeTable>) {
        try {
            maxSession = DataHandler.getCurMaxSession()
            val sb = Constants.NEXT_DAY_STATUS + mAppWidgetId
            courseCall.back(DataHandler.getWeekCourse(getBoolean(sb, false)))
            bTimeTableDCall.back(DataHandler.getCurTimeTable())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBindView(mContext: Context, courseList: List<Course>, timeTable: BTimeTable): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.item_week_widget_img)
        try {
            remoteViews.setImageViewBitmap(R.id.bg_iv, createWeekBitmap(PaintConfig(), courseList, timeTable, maxSession))
        }catch (e:Exception){}
        return remoteViews
    }
}