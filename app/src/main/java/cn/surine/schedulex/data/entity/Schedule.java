package cn.surine.schedulex.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-22 18:53
 */

@Entity
public class Schedule extends BaseVm {
    @PrimaryKey(autoGenerate = true)
    public int roomId;

    /**
     * 课程表名字
     */
    public String name;

    /**
     * 总共多少周
     */
    public int totalWeek;

    /**
     * 当前周
     */
    public int curWeek = 1;


    /**
     * 主颜色
     */
    public String color;


    /**
     * 背景图
     */
    public String imageUrl;


    public String getTotalWeekStr() {
        return "Total_" + totalWeek + "_Week";
    }

    public String getCurWeekStr() {
        return "Current Week:" + curWeek;
    }
}
