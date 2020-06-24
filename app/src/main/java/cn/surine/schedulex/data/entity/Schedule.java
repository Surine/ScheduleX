package cn.surine.schedulex.data.entity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

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
     * 开学时间 yyyyMMdd
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
     */
    public boolean lightText;


    /**
     * 是否显示周末
     */
    public boolean isShowWeekend = true;


    /**
     * 课程格子不透明度0-10
     */
    public int alphaForCourseItem = 10;


    /**
     * 最大节次
     */
    public int maxSession = 12;


    /**
     * 课程高度
     */
    public int itemHeight = 60;


    /**
     * 导入方式
     */
    public int importWay = 0;

    /**
     * 时间表id
     */
    public long timeTableId = 1;

    /**
     * 是否显示时间
     */
    public boolean isShowTime = true;

    /**
     * 课程主题
     * */
    public long courseThemeId;

    @SuppressLint("StringFormatMatches")
    public String getTotalWeekStr() {
        return App.context.getString(R.string.total_week, totalWeek);
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    public String getCurWeekStr() {
        return App.context.getString(R.string.current_week, curWeek());
    }


    public String getScheduleItemSubtitle() {
        return getTotalWeekStr() + " " + getCurWeekStr();
    }


    public String getAlphaForCourseItemText() {
        return "L" + alphaForCourseItem;
    }


    public String getMaxSessionItemText() {
        return maxSession + "节";
    }

    public String getItemSessionHeight() {
        return itemHeight + "dp";
    }

    public String getIsShowTimeTableStr() {
        return isShowWeekend ? App.context.getString(R.string.show_time) : App.context.getString(R.string.not_show_weekend);
    }

    public int curWeek() {
        return (Dates.getDateDif(Dates.getDate(Dates.yyyyMMdd), termStartDate) / 7) + 1;
    }

    public int lightTextColor() {
        return lightText ? Color.WHITE : Color.BLACK;
    }


    public static class IMPORT_WAY {
        //手动
        public static final int ADD = 0;
        //教务
        public static final int JW = 1;
        //json
        public static final int JSON = 2;
        //excel
        public static final int EXCEL = 3;
        //超表
        public static final int SUPER_CN = 4;
    }


    public Drawable getImportWayIcon() {
        int d;
        switch (importWay) {
            case IMPORT_WAY.ADD:
            d = R.mipmap.other;
            break;
            case IMPORT_WAY.JW:
            d = R.mipmap.way_jw;
            break;
            case IMPORT_WAY.JSON:
            d = R.mipmap.way_json;
            break;
            case IMPORT_WAY.EXCEL:
            d = R.mipmap.way_excel;
            break;
            case IMPORT_WAY.SUPER_CN:
            d = R.mipmap.super_class;
            break;
            default:
            d = R.mipmap.other;
            break;
        }
        return App.context.getDrawable(d);
    }
}