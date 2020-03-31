package cn.surine.schedulex.super_import.model;

import androidx.annotation.Keep;

import java.util.List;

import cn.surine.schedulex.data.entity.BaseVm;

@Keep
public class SuperCourseList extends BaseVm {
    public String endSchoolYear;
    public List<SuperCourse> lessonList;
    public String startSchoolYear;

    @Override
    public String toString() {
        return "SuperCourseList{" +
                "endSchoolYear='" + endSchoolYear + '\'' +
                ", lessonList=" + lessonList +
                ", startSchoolYear='" + startSchoolYear + '\'' +
                '}';
    }
}
