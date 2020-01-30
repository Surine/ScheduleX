package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.surine.schedulex.base.utils.Strs;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;

public class ScheduleViewModel extends ViewModel {

    private MutableLiveData<List<Course>> courseList;

    private ScheduleRepository scheduleRepository;

    public ScheduleViewModel(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public MutableLiveData<List<Course>> getCourseList() {
        if (courseList == null) {
            courseList = new MutableLiveData<>();
        }
        return courseList;
    }


    /**
     * 添加课表
     */
    public long addSchedule(String name, int totalWeek, int curWeek) {
        Schedule schedule = new Schedule();
        schedule.name = name;
        schedule.totalWeek = totalWeek == 0 ? 24 : totalWeek;
        schedule.curWeek = curWeek == 0 ? 1 : curWeek;
        return scheduleRepository.addSchedule(schedule);
    }

    /**
     * 通过id获取课表
     */
    public Schedule getScheduleById(long id) {
        return scheduleRepository.getScheduleById(id);
    }


    /**
     * 通过id 删除课表
     */
    public void deleteScheduleById(long scheduleId) {
        scheduleRepository.deleteScheduleById(scheduleId);
    }

    /**
     * 获取当前选中的课程表
     */
    public Schedule getCurSchedule() {
        return scheduleRepository.getCurSchedule();
    }


    /**
     * 获取 所有周 UI展示课表
     *
     * @param unHandleData 未处理的数据
     * @param curSchedule  当前选中课表
     */
    public List<List<Course>> getAllCourseUiData(List<List<Course>> unHandleData, Schedule curSchedule) {
        @SuppressLint("UseSparseArrays") Map<Integer, List<Course>> map = new HashMap<>(curSchedule.totalWeek);
        List<List<Course>> cl = new ArrayList<>();
        for (int i = 0; i < unHandleData.size(); i++) {
            //处理后的列表
            List<Course> handleData = new ArrayList<>();
            //初始化
            for (int j = 0; j < 40; j++) {
                handleData.add(null);
            }
            //处理每周数据
            for (int j = 0; j < unHandleData.get(i).size(); j++) {
                Course course = unHandleData.get(i).get(j);
                if (!(Strs.equals(course.classDay, "6") || Strs.equals(course.classDay, "7"))) {
                    //持续2节的添加1个空格，持续4节的添加2个空格
                    if (Integer.parseInt(course.continuingSession) == 4) {
                        handleData.set(Integer.parseInt(course.classSessions) * 5 + (Integer.parseInt(course.classDay) - 1), course);
                    }
                    handleData.set((Integer.parseInt(course.classSessions) - 1) * 5 + (Integer.parseInt(course.classDay) - 1), course);
                }
            }
            cl.add(handleData);
        }
        return cl;
    }

    public List<Schedule> getSchedules() {
        return scheduleRepository.getSchedules();
    }



}
