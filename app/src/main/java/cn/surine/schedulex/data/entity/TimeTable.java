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
}
