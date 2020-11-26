package cn.surine.schedulex.app_widget.core;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.interfaces.DCall;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：负责给日视图加载数据
 *
 * @author sunliwei
 * @date 2020/5/29 18:10
 */
public abstract class DailyRemoteViewService extends RemoteViewsService {

    public List<Course> courseList = new ArrayList();
    private BTimeTable timeTable;


    private class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private int mAppWidgetId;
        private Context mContext;

        public RemoteViewsFactory(Context context, Intent intent) {
            this.mContext = context;
            this.mAppWidgetId = intent.getIntExtra(Constants.APP_WIDGET_ID, -1);
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            dataSetChanged(mAppWidgetId, courses -> courseList = courses, bTimeTable -> timeTable = bTimeTable);
        }

        @Override
        public void onDestroy() {
            try {
                courseList.clear();
            } catch (Exception ignored) {
                courseList = null;
                if (courseList == null) {
                    courseList = new ArrayList<>();
                }
            }
        }

        @Override
        public int getCount() {
            return courseList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Course course = null;
            try {
                course = courseList.get(position);
            } catch (Exception e) {
                course = new Course();
            }
            if("".equals(course.classSessions)){
                course.classSessions = "1";
            }
            BTimeTable.BTimeInfo data = timeTable.timeInfoList.get(Integer.parseInt(course.classSessions) - 1);
            return onBindView(mContext, course, data);
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    /**
     * 更新数据
     */
    protected abstract void dataSetChanged(int mAppWidgetId, DCall<List<Course>> courseCall, DCall<BTimeTable> bTimeTableDCall);

    /**
     * 绑定视图
     */
    protected abstract RemoteViews onBindView(Context mContext, Course course, BTimeTable.BTimeInfo data);

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(this, intent);
    }
}
