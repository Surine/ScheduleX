package cn.surine.schedulex.ui.course;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.controller.BaseRepository;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.CourseList;
import cn.surine.schedulex.data.network.Loader;
import io.reactivex.Flowable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-19 22:51
 */
public class CourseRepository extends BaseRepository {

    public static AbstractSingleTon<CourseRepository> abt = new AbstractSingleTon<CourseRepository>() {
        @Override
        protected CourseRepository newObj(Bundle bundle) {
            return new CourseRepository();
        }
    };


    Flowable<CourseList> getCourseByNet(int week){
        return Loader.getInstance().getService().getSchedule(week).compose(schedulerHelper());
    }

    void saveCourseByDb(List<Course> courseList){
        for (Course course :courseList) {
            appDatabase.courseDao().insert(course);
        }
    }

    Course getCourseByDb(int id){
        return  appDatabase.courseDao().getById(id);
    }


    List<Course> queryCourseByWeek(int week, int scheduleId){
        return appDatabase.courseDao().getByWeek(week,scheduleId);
    }


    void clearCourseByDb() {
        appDatabase.courseDao().deleteAll();
    }


    public void deleteCourseByScheduleId(long scheduleId) {
        appDatabase.courseDao().delete(scheduleId);
    }
}
