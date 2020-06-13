package cn.surine.schedulex.data.helper;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.base.controller.AbstractSingleTon;
import cn.surine.schedulex.base.utils.DataMaps;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.ui.course.CourseRepository;
import cn.surine.schedulex.ui.schedule.ScheduleRepository;
import cn.surine.schedulex.ui.timetable_list.TimeTableRepository;

/**
 * Intro：
 * 这个类用于快速获得某些数据，但直接依赖了各仓库，原则上不符合操作逻辑，但是可以方便
 * 某些不适合依赖于ViewModel的类，如小部件等。
 * <p>
 * 在常规的UI界面中，我们还是应该需要使用ViewModel来构建数据以保证正常的数据的时效性
 * <p>
 * 同时，不依赖于ViewModel和仓库的数据处理，也被移到了这个类里。
 *
 * @author sunliwei
 * @date 2020/5/29 18:22
 */
public class DataHandler {
    public static AbstractSingleTon<DataHandler> abt = new AbstractSingleTon<DataHandler>() {
        @Override
        protected DataHandler newObj(Bundle bundle) {
            return new DataHandler();
        }
    };

    private CourseRepository courseRepository = CourseRepository.abt.getInstance();
    private ScheduleRepository scheduleRepository = ScheduleRepository.abt.getInstance();
    private TimeTableRepository timeTableRepository = TimeTableRepository.abt.getInstance();


    /**
     * 获取某天的课程
     *
     * @param day      星期几？
     * @param nextWeek 是否下周？
     */
    public List<Course> getCourseList(int day, boolean nextWeek) {
        Schedule curSchedule = scheduleRepository.getCurSchedule();
        return courseRepository.getTodayCourseListByScheduleId(day, nextWeek ? curSchedule.curWeek() + 1 : curSchedule.curWeek(), curSchedule.roomId);
    }

    /**
     * 获取当前课表的时间表
     */
    public BTimeTable getCurTimeTable() {
        Schedule curSchedule = scheduleRepository.getCurSchedule();
        return DataMaps.dataMappingTimeTableToBTimeTable(timeTableRepository.getTimeTableById(curSchedule.timeTableId));
    }

    /**
     * 获取某周课程
     *
     * @param nextWeek 周
     */
    public List<Course> getWeekCourse(boolean nextWeek) {
        Schedule schedule = scheduleRepository.getCurSchedule();
        int curWeek = schedule.curWeek();
        return courseRepository.getWeekCourseListByScheduleId(nextWeek ? curWeek + 1 : curWeek, schedule.roomId);
    }


    /**
     * 获得当前课表的最大节次
     */
    public int getCurMaxSession() {
        return scheduleRepository.getCurSchedule().maxSession;
    }


    /**
     * 根据提供的课程数据获取某一天的课程
     */
    public List<Course> getOneDayCourse(int day, List<Course> courses) {
        List<Course> handlers = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.classDay.equals(String.valueOf(day))) {
                handlers.add(course);
            }
        }
        return handlers;
    }


    public Schedule getCurSchedule() {
        return scheduleRepository.getCurSchedule();
    }
}
