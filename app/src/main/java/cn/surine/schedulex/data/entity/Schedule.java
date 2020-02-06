package cn.surine.schedulex.data.entity;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;
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



    /**
     * 是否使用亮色文本
     * */
    public boolean lightText;


    @SuppressLint("StringFormatMatches")
    public String getTotalWeekStr() {
        return App.context.getString(R.string.total_week,totalWeek);
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    public String getCurWeekStr() {
        return App.context.getString(R.string.current_week,curWeek());
    }


    public int curWeek(){
        return (Dates.getDateDif(Dates.getDate(Dates.yyyyMMdd),termStartDate) / 7) + 1;
    }

    public int lightTextColor(){
        return lightText ? Color.WHITE : Color.BLACK;
    }
}
