package cn.surine.schedulex.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-15 20:23
 */

@Entity
public class Course extends BaseVm {

    @PrimaryKey(autoGenerate = true)
    public int roomId;
    public String coureNumber;

    //name （傻逼jw后端单词都不会拼）
    public String coureName = "";
    public String teacherName = "UnKnown";

    //week
    public String classWeek = "";

    //day time
    public String classDay = "";
    public String classSessions = "";
    public String continuingSession = "";
    public String weekDescription = "";

    //position
    public String campusName = "";
    public String teachingBuildingName = "";
    public String classroomName = "";

    //properties
    public String coursePropertiesName = "";

    //score point
    public String xf = "";

    //课表外键，属于哪个课表
    public long scheduleId;

    //属于那一周
    public int belongsToWeek;

    //色值
    public String color;


    public Course(int roomId, String coureName, String teacherName, String classWeek, String classDay, String classSessions, String continuingSession, String weekDescription, String campusName, String teachingBuildingName, String classroomName, String coursePropertiesName, String xf, long scheduleId, int belongsToWeek) {
        this.roomId = roomId;
        this.coureName = coureName;
        this.teacherName = teacherName;
        this.classWeek = classWeek;
        this.classDay = classDay;
        this.classSessions = classSessions;
        this.continuingSession = continuingSession;
        this.weekDescription = weekDescription;
        this.campusName = campusName;
        this.teachingBuildingName = teachingBuildingName;
        this.classroomName = classroomName;
        this.coursePropertiesName = coursePropertiesName;
        this.xf = xf;
        this.scheduleId = scheduleId;
        this.belongsToWeek = belongsToWeek;
    }

    /**
     * item数据
     * */
    public String itemData() {
        return coureName + "\n" + teachingBuildingName + classroomName;
    }



    @Override
    public String toString() {
        return "Course{" +
                "roomId=" + roomId +
                ", courseName='" + coureName + '\'' +
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
                ", belongsToWeek=" + belongsToWeek +
                '}';
    }
}
