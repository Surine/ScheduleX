package cn.surine.schedulex.app_widget.little_daily

import android.content.Context
import android.widget.RemoteViews
import cn.surine.coursetableview.entity.BTimeTable
import cn.surine.coursetableview.entity.BTimeTable.BTimeInfo
import cn.surine.schedulex.R
import cn.surine.schedulex.app_widget.core.DailyRemoteViewService
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.base.interfaces.DCall
import cn.surine.schedulex.base.utils.Dates
import cn.surine.schedulex.base.utils.Prefs.getBoolean
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.helper.DataHandler

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/31 21:35
 */
class LittleDailyService : DailyRemoteViewService() {
    //这里回调数据
    override fun dataSetChanged(mAppWidgetId: Int, courseCall: DCall<List<Course>>, bTimeTableDCall: DCall<BTimeTable>) {
        val sb = Constants.NEXT_DAY_STATUS + mAppWidgetId
        val isNextDay = getBoolean(sb, false)
        val today = Dates.getWeekDay()
        val nextDay = if (Dates.getWeekDay() + 1 % 7 == 0) 7 else (Dates.getWeekDay() + 1) % 7
        val day = if (isNextDay) nextDay else today
        val nextWeek = nextDay == 1
        try {
            courseCall.back(DataHandler.getCourseList(day, nextWeek))
            bTimeTableDCall.back(DataHandler.getCurTimeTable())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //这里绑定视图
    override fun onBindView(mContext: Context, course: Course, data: BTimeInfo): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.item_widget_day_class_little)
        //此处不能删除，会出复用问题
        if (course != null) {
            remoteViews.setTextViewText(R.id.day_class_title, course.coureName)
            val sb = course.teachingBuildingName + course.classroomName
            remoteViews.setTextViewText(R.id.day_class_subtitle, sb)
            val sb2 = course.classSessions + "-" + (course.classSessions.toInt() + course.continuingSession.toInt() - 1)
            remoteViews.setTextViewText(R.id.day_class_session, sb2)
            remoteViews.setTextViewText(R.id.widget_day_class_course_time, data.startTime)
        } else {
            val str = ""
            remoteViews.setTextViewText(R.id.day_class_title, str)
            remoteViews.setTextViewText(R.id.day_class_subtitle, str)
            remoteViews.setTextViewText(R.id.day_class_session, str)
            remoteViews.setTextViewText(R.id.widget_day_class_course_time, str)
        }
        return remoteViews
    }
}