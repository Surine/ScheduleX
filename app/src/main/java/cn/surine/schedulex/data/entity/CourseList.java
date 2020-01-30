package cn.surine.schedulex.data.entity;

import java.util.List;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:42
 */
public class CourseList extends BaseVm {
    //当前周
    public int nowWeek;
    //当周课表
    public List<Course> courseList;
    //共多少周
    public int weeks;
}
