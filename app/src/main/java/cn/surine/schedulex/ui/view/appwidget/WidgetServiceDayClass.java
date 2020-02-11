package cn.surine.schedulex.ui.view.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;

public class WidgetServiceDayClass extends RemoteViewsService {
    private List<Course> courseList = new ArrayList<>();
    private ScheduleRepository scheduleRepository = ScheduleRepository.abt.getInstance();
    private CourseRepository courseRepository = CourseRepository.abt.getInstance();

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(this, intent);
    }

    private class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private int mAppWidgetId;


        public RemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_day_class);
            Course course = courseList.get(position);
            if (course != null) {
                rv.setTextViewText(R.id.day_class_title, course.coureName);
                rv.setTextViewText(R.id.day_class_subtitle, course.teachingBuildingName + course.classroomName);
                rv.setTextViewText(R.id.day_class_session, course.classSessions + "-" + (Integer.parseInt(course.classSessions) + Integer.parseInt(course.continuingSession) - 1));
            } else {
                rv.setTextViewText(R.id.day_class_title, "");
                rv.setTextViewText(R.id.day_class_subtitle, "");
                rv.setTextViewText(R.id.day_class_session, "");
            }
            return rv;
        }


        @Override
        public int getCount() {
            return courseList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
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
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            try {
                Schedule schedule = scheduleRepository.getCurSchedule();
                courseList = courseRepository.getTodayCourseListByScheduleId(Dates.getWeekDay() == 0 ? 7 : Dates.getWeekDay(),schedule.curWeek(),schedule.roomId);
                Log.d("slw", "onDataSetChanged: "+courseList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            courseList.clear();
        }


    }

}
