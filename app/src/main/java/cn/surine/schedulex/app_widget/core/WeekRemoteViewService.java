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
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/8 15:34
 */
public abstract class WeekRemoteViewService extends RemoteViewsService {

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
            courseList.clear();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            return onBindView(mContext, courseList, timeTable);
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
    protected abstract RemoteViews onBindView(Context mContext, List<Course> courseList, BTimeTable timeTable);

    @Override
    public WeekRemoteViewService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WeekRemoteViewService.RemoteViewsFactory(this, intent);
    }
}
