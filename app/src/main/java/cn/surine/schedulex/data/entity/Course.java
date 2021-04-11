package cn.surine.schedulex.data.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.utils.Dates;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-15 20:23
 */

@Entity
public class Course extends BaseVm {

    /**
     * 课程id
     */
    @PrimaryKey
    @NonNull
    public String id = "";

    //name （傻逼jw后端单词都不会拼）
    public String coureNumber;
    public String coureName = "";
    public String teacherName = "UnKnown";

    //week  ex:100101011111
    public String classWeek = "";

    //day time
    //ex : classDay的范围 1 2 3 4 5 6 7
    public String classDay = "";
    public String classSessions = "";
    public String continuingSession = "";
    public String weekDescription = "";

    //position
    public String campusName = "";
    //主内容
    public String teachingBuildingName = "";
    public String classroomName = "";

    //properties
    public String coursePropertiesName = "";

    //score point
    public String xf = "";

    //课表外键，属于哪个课表
    public long scheduleId;


    //色值
    public String color;

    //备忘录
    public String memo = "";


    /**
     * item数据
     */
    public String itemData() {
        return coureName + "\n" + teachingBuildingName + classroomName;
    }


    public String getWeekDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < classWeek.length(); i++) {
            if (classWeek.charAt(i) == '1') {
                stringBuilder.append(i + 1);
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.append(App.context.getString(R.string.week, "")).toString();
    }


    public String getClassDayDescription() {
        return App.context.getString(R.string.weekday, Dates.getWeekInChi(Integer.parseInt(classDay)));
    }


    public String getSessionDescription() {
        return classSessions + "-" + (Integer.parseInt(classSessions) + Integer.parseInt(continuingSession) - 1) + App.context.getString(R.string.session);
    }

    public String getPositionDescription(){
        String result =  campusName + teachingBuildingName + classroomName;
        if(TextUtils.isEmpty(result)){
            return "无位置";
        }
        return result;
    }

    public String getTeacherName(){
        if(TextUtils.isEmpty(teacherName)){
            return "未知";
        }else{
            return teacherName;
        }
    }

    public String getScore(){
        if(TextUtils.isEmpty(xf)){
            return "未知";
        }else{
            return xf + "分";
        }
    }


    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", coureNumber='" + coureNumber + '\'' +
                ", coureName='" + coureName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", classWeek='" + classWeek + '\'' +
                ", classDay='" + classDay + '\'' +
                ", classSessions='" + classSessions + '\'' +
                ", continuingSession='" + continuingSession + '\'' +
                ", weekDescription='" + weekDescription + '\'' +
                ", campusName='" + campusName + '\'' +
                ", teachingBuildingName='" + teachingBuildingName + '\'' +
                ", classroomName='" + classroomName + '\'' +
                ", coursePropertiesName='" + coursePropertiesName + '\'' +
                ", xf='" + xf + '\'' +
                ", scheduleId=" + scheduleId +
                ", color='" + color + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}