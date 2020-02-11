package cn.surine.schedulex.ui.schedule;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.base.utils.Dates;
import cn.surine.schedulex.base.utils.Strs;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;

public class ScheduleViewModel extends ViewModel {

    private MutableLiveData<List<Course>> courseList;

    private ScheduleRepository scheduleRepository;

    public ScheduleViewModel(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    /**
     * 添加课表
     */
    public long addSchedule(String name, int totalWeek, int curWeek) {
        Schedule schedule = new Schedule();
        schedule.name = name;
        schedule.totalWeek = totalWeek == 0 ? 24 : totalWeek;
        schedule.color = Constants.COLOR_1[new Random(System.currentTimeMillis()).nextInt(Constants.COLOR_1.length)];
        schedule.termStartDate = Dates.getTermStartDate(curWeek == 0 ? 1 : curWeek);
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
                if(TextUtils.isEmpty(course.classDay)){
                    continue;
                }
                if (!(Strs.equals(course.classDay, "6") || Strs.equals(course.classDay, "7"))) {
                    //持续2节的添加1个空格，持续4节的添加2个空格
                    if (Integer.parseInt(course.continuingSession) == 4) {
                        handleData.set(divide(course.classSessions) * 5 + (Integer.parseInt(course.classDay) - 1), course);
                    }
                    handleData.set((divide(course.classSessions) - 1) * 5 + (Integer.parseInt(course.classDay) - 1), course);
                }
            }
            cl.add(handleData);
        }
        return cl;
    }

    //按照tust课时操作来实现
    private int divide(String classSessions) {
        return (Integer.parseInt(classSessions) + 1) /2;
    }

    public List<Schedule> getSchedules() {
        return scheduleRepository.getSchedules();
    }



    /**
     * 更新课表
     * @param schedule 课表
     * */
    public void updateSchedule(Schedule schedule) {
        scheduleRepository.updateSchedule(schedule);
    }


    /**
     * 获取课表数量
     * */
    public int getSchedulesNumber() {
        return scheduleRepository.getSchedulesNumber();
    }
}
