package cn.surine.schedulex.base.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Intro：时间工具类
 *
 * @author sunliwei
 * @date 2020-01-22 16:39
 */
public class Dates {

    /**
     * 获取今天星期几的数字
     * 0 = 星期日
     */
    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 获取月份的英语
     */
    public static String getMonthInEng() {
        switch (getDate("MM")) {
            case "01":
                return "Jan.";
            case "02":
                return "Feb.";
            case "03":
                return "Mar.";
            case "04":
                return "Apr.";
            case "05":
                return "May.";
            case "06":
                return "Jun.";
            case "07":
                return "Jul.";
            case "08":
                return "Aug.";
            case "09":
                return "Sept.";
            case "10":
                return "Oct.";
            case "11":
                return "Nov.";
            case "12":
                return "Dec.";
            default:
                return "unknown";
        }
    }


    /**
     * 获取日期
     *
     * @param format 格式
     */
    public static String getDate(String format) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getDate("MM"));
    }
}
