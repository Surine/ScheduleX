package cn.surine.coursetableview.entity;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surine on 2019/2/25.
 * 课程实体基础类
 */

@Keep
public class BCourse implements Serializable {
    private String id;
    private long scheduleId;
    /*课程名*/
    private String name = "";
    /*上课地点*/
    private String position = "";
    /*老师*/
    private String teacher = "";
    /*周几上*/
    private int day = 1;
    /*第几节开始*/
    private int sectionStart = 0;
    /*持续几节*/
    private int sectionContinue = 1;
    /*课程颜色*/
    private String color;
    /*第几周到第几周上*/
    private List<Integer> week = new ArrayList<>();
    /**
     * 学分
     */
    public String score;

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        if (name.length() > 6) {
            return name.substring(0, 6) + "...";
        }
        return name;
    }


    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }


    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position != null) {
            this.position = position;
        }
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        if (teacher != null) {
            this.teacher = teacher;
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day >= 1 && day <= 7) {
            this.day = day;
        }
    }

    public int getSectionStart() {
        return sectionStart;
    }

    public void setSectionStart(int sectionStart) {
        //按照1标志位开始
        if (sectionStart >= 1)
            this.sectionStart = sectionStart;
    }

    public int getSectionContinue() {
        return sectionContinue;
    }

    public void setSectionContinue(int sectionContinue) {
        if (sectionContinue >= 1) {
            this.sectionContinue = sectionContinue;
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Integer> getWeek() {
        return week;
    }

    public void setWeek(List<Integer> week) {
        if (week != null && week.size() >= 1) {
            this.week = week;
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", teacher='" + teacher + '\'' +
                ", day=" + day +
                ", sectionStart=" + sectionStart +
                ", sectionContinue=" + sectionContinue +
                ", color=" + color +
                ", week=" + week +
                '}';
    }
}
