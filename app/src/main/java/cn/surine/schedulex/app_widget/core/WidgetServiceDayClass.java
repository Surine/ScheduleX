package cn.surine.schedulex.app_widget.core;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.R;
import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Prefs;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository;

public class WidgetServiceDayClass extends RemoteViewsService {
    public List<Course> courseList = new ArrayList();
    public CourseRepository courseRepository = CourseRepository.abt.getInstance();
    public ScheduleRepository scheduleRepository = ScheduleRepository.abt.getInstance();
    public TimeTableRepository timeTableRepository = TimeTableRepository.abt.getInstance();
    private BTimeTable timeTable;

    private class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private int mAppWidgetId;
        private Context mContext;

        @Override
        public long getItemId(int i) {
            return (long) i;
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

        public RemoteViewsFactory(Context context, Intent intent) {
            this.mContext = context;
            this.mAppWidgetId = intent.getIntExtra(Constants.APP_WIDGET_ID, -1);
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.item_widget_day_class);
            Course course = courseList.get(i);
            if (course != null) {
                remoteViews.setTextViewText(R.id.day_class_title, course.coureName);
                StringBuilder sb = new StringBuilder();
                sb.append(course.teachingBuildingName);
                sb.append(course.classroomName);
                remoteViews.setTextViewText(R.id.day_class_subtitle, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(course.classSessions);
                sb2.append("-");
                sb2.append((Integer.parseInt(course.classSessions) + Integer.parseInt(course.continuingSession)) - 1);
                remoteViews.setTextViewText(R.id.day_class_session, sb2.toString());
                BTimeTable.BTimeInfo data = timeTable.timeInfoList.get(Integer.parseInt(course.classSessions) - 1);
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

        @Override
        public int getCount() {
            return courseList.size();
        }

        @Override
        public void onDataSetChanged() {
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.NEXT_DAY_STATUS);
            sb.append(this.mAppWidgetId);
            boolean isNextDay = Prefs.getBoolean(sb.toString(), false);
            int today = Dates.getWeekDay();
            int nextDay = Dates.getWeekDay() + 1 % 7 == 0 ? 7 : (Dates.getWeekDay() + 1) % 7;
            int day = isNextDay ? nextDay : today;
            boolean nextWeek = (nextDay == 1);
            try {
                Schedule curSchedule = scheduleRepository.getCurSchedule();
                courseList = courseRepository.getTodayCourseListByScheduleId(day, nextWeek ? curSchedule.curWeek() + 1 : curSchedule.curWeek(), curSchedule.roomId);
                timeTable = DataMaps.dataMappingTimeTableToBTimeTable(timeTableRepository.getTimeTableById(curSchedule.timeTableId));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            courseList.clear();
        }
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory(this, intent);
    }
}
