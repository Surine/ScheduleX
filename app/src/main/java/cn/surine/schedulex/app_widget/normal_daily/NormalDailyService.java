package cn.surine.schedulex.app_widget.normal_daily;

import android.content.Context;
import android.widget.RemoteViews;

import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.core.DailyRemoteViewService;
import cn.surine.schedulex.data.helper.DataHandler;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.interfaces.DCall;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/31 21:35
 */
public class NormalDailyService extends DailyRemoteViewService {

    //这里回调数据
    @Override
    protected void dataSetChanged(int mAppWidgetId, DCall<List<Course>> courseCall,DCall<BTimeTable> bTimeTableDCall) {
        String sb = Constants.NEXT_DAY_STATUS + mAppWidgetId;
        boolean isNextDay = Prefs.getBoolean(sb, false);
        int today = Dates.getWeekDay();
        int nextDay = Dates.getWeekDay() + 1 % 7 == 0 ? 7 : (Dates.getWeekDay() + 1) % 7;
        int day = isNextDay ? nextDay : today;
        boolean nextWeek = (nextDay == 1);
        try {
            if(courseCall!= null){
                courseCall.back(DataHandler.abt.getInstance().getCourseList(day,nextWeek));
            }
            if(bTimeTableDCall != null){
                bTimeTableDCall.back(DataHandler.abt.getInstance().getCurTimeTable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //这里绑定视图
    @Override
    protected RemoteViews onBindView(Context mContext, Course course, BTimeTable.BTimeInfo data) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_day_class);
        if (course != null) {
            remoteViews.setTextViewText(R.id.day_class_title, course.coureName);
            String sb = course.teachingBuildingName + course.classroomName;
            remoteViews.setTextViewText(R.id.day_class_subtitle, sb);
            String sb2 = course.classSessions + "-" + ((Integer.parseInt(course.classSessions) + Integer.parseInt(course.continuingSession)) - 1);
            remoteViews.setTextViewText(R.id.day_class_session, sb2);
            remoteViews.setTextViewText(R.id.widget_day_class_course_time, data.startTime);
        } else {
            String str = "";
            remoteViews.setTextViewText(R.id.day_class_title, str);
            remoteViews.setTextViewText(R.id.day_class_subtitle, str);
            remoteViews.setTextViewText(R.id.day_class_session, str);
            remoteViews.setTextViewText(R.id.widget_day_class_course_time, str);
        }
        return remoteViews;
    }
}
