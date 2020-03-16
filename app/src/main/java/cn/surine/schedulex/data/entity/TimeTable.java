package cn.surine.schedulex.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Intro：
 * 时间表
 *
 * @author sunliwei
 * @date 2020-02-11 17:55
 */

@Entity
public class TimeTable extends BaseVm {

    @PrimaryKey(autoGenerate = true)
    public int roomId;


    /**
     * timetable 名称
     */
    public String name;


    /**
     * 起始 时间格式  小时 * 60 + 分钟
     */
    public long startTime;

    /**
     * 规则
     * {45,10,45,10,45……}
     */
    public String rule;


    public TimeTable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", rule='" + rule + '\'' +
                '}';
    }


    /**
     * 获取时间表支持的节数
     */
    public int getSessionNum() {
        return rule.split(",").length / 2;
    }

    /**
     * 默认时间表
     */
    public static TimeTable tedaNormal() {
        TimeTable timeTable = new TimeTable("默认时间表_1");
        timeTable.roomId = 0;
        timeTable.startTime = 8 * 60 + 20;
        timeTable.rule = "45,10,45,20,45,10,45,120,45,10,45,15,45,10,45,55,45,10,45,15,45,10,45,0";
        return timeTable;
    }
}
