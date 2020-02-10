package cn.surine.coursetableview.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Surine on 2019/2/25.
 * 时间工具类
 */

public class TimeUtil {

    public static final String yyyyMMdd = "yyyy-MM-dd";


    /**
     * 当前周几？
     * */
    public static int getWeekDay() {
        int w = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        return w <= 0 ? 7 : w;
    }

    /**
     * 当前几月？
     * */
    public static int getMonth(){
        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return m;
    }


    /**
     * 时间字符串比较大小
     * @param s1 时间1
     * @param s2 时间2
     * @param format 格式
     * @return 1:s1大于s2   -1:s1小于s2
     */
    public static int compareDate(String s1,String s2,String format){
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long sTime1,sTime2;
        try {
            sTime1 = sd.parse(s1).getTime();
            sTime2 = sd.parse(s2).getTime();

            if(sTime1 < sTime2){
                return -1;
            }

            if(sTime1 == sTime2){
                return 0;
            }

            if(sTime1 > sTime2){
                return 1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取一个时间的前几天或者后几天
     * @param dateString 时间字符串
     * @param format 格式
     * @param number 几天？
     * @param isAfter 前几天还是后几天 true 为后，false为前
     * @return 处理后时间字符串
     * */
    public static String getDateBeforeOrAfter(String dateString,String format,int number,boolean isAfter){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        Date date1 = null;
        try {
            date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 正数表示该日期后n天，负数表示该日期的前n天
            calendar.add(Calendar.DATE, isAfter ? number :( -1 * number));
            date1 = calendar.getTime();
            return sdf.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }


    /**
     * CourseTableView 日期计算工具
     * @param curTermStartDate 当前学期起始日期
     * @param currentWeek 选中的当前周
     * @param i 第 {currentWeek} 周 星期几（i）
     * */
    public static String getTodayInfoString(String curTermStartDate, int currentWeek, int i) {
        String day = getDateBeforeOrAfter(curTermStartDate,yyyyMMdd,(currentWeek - 1) * 7 + (i - 1),true);
        return getStringByTimeString(day,"dd");
    }


    /**
     * 月份个性提示串
     * */
    public static String getWeekInfoString(String curTermStartDate, int currentWeek) {
        String day = getDateBeforeOrAfter(curTermStartDate,yyyyMMdd,(currentWeek - 1) * 7,true);
        return getStringByTimeString(day,"MM");
    }


    /**
     * 获取某天是星期几
     * */
    public static int getWeekDayByString(String curTermStartDate) {
       SimpleDateFormat sd = new SimpleDateFormat(yyyyMMdd);
        Calendar c = Calendar.getInstance();
        int dayForWeek = 0;
        try {
            c.setTime(sd.parse(curTermStartDate));
            if(c.get(Calendar.DAY_OF_WEEK) == 1){
                dayForWeek = 7;
            }else{
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayForWeek;
    }


    /**
     * 通过时间字符串解析时间值
     * */
    public static String getStringByTimeString(String dateString,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
        SimpleDateFormat formatResult = new SimpleDateFormat(pattern);//设置日期格式
        Date date;
        try {
            date = format.parse(dateString);
            return formatResult.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * 看是否合法
     * @param curTermStartDate
     * */
    public static boolean isVaild(String curTermStartDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd);
        try {
            Date date = sdf.parse(curTermStartDate);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前真实周
     * @param curTermStartDate 开学日期
     * @return 返回负值代表开学日期还没到，这是开学前 i 周
     * 返回正值代表开学日期在过去，这是开学第 i 周
     * */
    public static int getRealWeek(String curTermStartDate) throws ParseException {

        if(TimeUtil.isVaild(curTermStartDate)){
            if(compareDate(curTermStartDate,"2000-1-1",TimeUtil.yyyyMMdd) == 1){
                if(getWeekDayByString(curTermStartDate) == 1){
                    SimpleDateFormat sd = new SimpleDateFormat(yyyyMMdd);
                    //解析开学日期
                    Date dateForTermStart = sd.parse(curTermStartDate);
                    long termStartTime = dateForTermStart.getTime();
                    long curTime = new Date().getTime();
                    //开学日期在未来
                    if(termStartTime > curTime){
                        long day = (termStartTime - curTime) / (24 * 60 * 60 * 1000);
                        return -1 * (int) (day / 7) + 1;
                    }else{
                        //开学日期在过去
                        long day = (curTime - termStartTime) / (24 * 60 * 60 * 1000);
                        return (int) (day / 7) + 1;
                    }
                }else{
                    throw new IllegalArgumentException("this date is not monday");
                }
            }
        }else{
            throw new IllegalArgumentException("please set a vaild time format : yyyy-MM-dd");
        }
        return 0;
    }
}
