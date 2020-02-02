package cn.surine.schedulex.base;

/**
 * Intro：
 * 静态常量
 *
 * @author sunliwei
 * @date 2020-01-17 10:46
 */
public class Constants {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "schedulex.db";

    /**
     * 登录返回值
     */
    public static final String LOGIN_SUCCESS = "0";
    public static final String ACCOUNT_NOT_EXIST = "1";
    public static final String PASSWORD_ERROR = "2";
    public static final String TOTAL_WEEK = "TOTAL_WEEK";

    public static final String IS_FIRST = "is_first";
    public static final String CUR_SCHEDULE = "cur_schedule";
    public static final String CONTENT = "content";
    public static final String FILE = "file";


    /**
     * 常规颜色值
     */
    public static final String NORMAL_COLOR = "#536DFE";
    public static final String[] COLOR_1 = new String[]{
            "#FF5252",
            "#FF4081",
            "#E040FB",
            "##536DFE",
            "#7C4DFF",
            "#40C4FF",
            "#18FFFF",
            "#B2FF59",
            "#FFFF00",
            "#FF6E40",
    };
}
