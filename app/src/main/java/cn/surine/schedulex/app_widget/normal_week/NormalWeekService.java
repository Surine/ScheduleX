package cn.surine.schedulex.app_widget.normal_week;

import android.content.Context;
import android.widget.RemoteViews;

import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.R;
import cn.surine.schedulex.app_widget.core.WeekRemoteViewService;
import cn.surine.schedulex.data.helper.DataHandler;
import cn.surine.schedulex.base.interfaces.DCall;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/8 15:08
 */
public class NormalWeekService extends WeekRemoteViewService {
    private int maxSession;

    @Override
    protected void dataSetChanged(int mAppWidgetId, DCall<List<Course>> courseCall, DCall<BTimeTable> bTimeTableDCall) {
        try {
            maxSession = DataHandler.abt.getInstance().getCurMaxSession();
            if(courseCall != null){
                courseCall.back(DataHandler.abt.getInstance().getWeekCourse(false));
            }
            if(bTimeTableDCall != null){
                bTimeTableDCall.back(DataHandler.abt.getInstance().getCurTimeTable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected RemoteViews onBindView(Context mContext, List<Course> courseList, BTimeTable timeTable) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_week_widget_img);
        remoteViews.setImageViewBitmap(R.id.bg_iv, BitmapMaker.createWeekBitmap(new PaintConfig(),courseList,timeTable,maxSession));
        return remoteViews;
    }
}
