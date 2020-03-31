package cn.surine.schedulex.base.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
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

    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String HHmm = "HH:mm";
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final long ONE_MIN = 60 * 1000;
    public static final long ONE_HOUR = ONE_MIN * 60;


    /**
     * 获取今天星期几的数字
     * 周一1  周二2  …… 周日7
     */
    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return 7;
        }
        return day - 1;
    }


    /**
     * 获取某一日对应的星期
     */
    public static int getWeekDay(String dateStr) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = f.parse(dateStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return 7;
        }
        return day - 1;
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


    /**
     * 根据字符串获取日期
     *
     * @param dateStr 字符串
     * @param format  格式
     */
    public static Date getDate(String dateStr, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取开学日期
     */
    public static String getTermStartDate(int curWeek) {
        int day = (curWeek - 1) * 7;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return getMondayByDate(now.getTime());
    }


    /**
     * 获取某周的周一
     */
    public static String getMondayByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return sdf.format(cal.getTime());
    }


    /**
     * 获取两个日期相差的天数
     *
     * @param date1
     * @param date2
     */
    public static int getDateDif(String date1, String date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate(date1, yyyyMMdd));
        long time1 = cal.getTimeInMillis();
        cal.setTime(getDate(date2, yyyyMMdd));
        long time2 = cal.getTimeInMillis();
        return (int) (time1 - time2) / (1000 * 3600 * 24);
    }


    /**
     * 根据时间戳返回时间字符串
     *
     * @param time   时间戳
     * @param format 表达式
     */
    public static String getDateStringByStamp(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(time);
    }


    /**
     * 将分为单位的时间转换为 HH:mm格式
     */
    public static String getTransformTimeNumber(long time) {
        time %= 60 * 24;
        long s1 = time / 60;
        long s2 = time - s1 * 60;
        StringBuilder stringBuilder = new StringBuilder();
        if (s1 < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(s1).append(":");
        if (s2 < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(s2);
        return stringBuilder.toString();
    }


    /**
     * 将HH:mm的时间转换为分为单位的数字
     */
    public static long getTransformTimeString(String time) {
        try {
            return Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
        } catch (Exception e) {
            return 0;
        }
    }

}
