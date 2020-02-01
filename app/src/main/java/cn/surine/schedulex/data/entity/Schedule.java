package cn.surine.schedulex.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import cn.surine.schedulex.base.utils.Dates;

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
     * 开学时间
     */
    public String termStartDate;


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
        return "Current Week:" +curWeek();
    }


    public int curWeek(){
        return (Dates.getDateDif(Dates.getDate(Dates.yyyyMMdd),termStartDate) / 7) + 1;
    }
}
