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
    public static final String TIMETABLE_IS_FIRST = "timetable_is_first";
    public static final String CUR_SCHEDULE = "cur_schedule";
    public static final String CONTENT = "content";
    public static final String FILE = "file";


    /**
     * 常规颜色值
     */
    public static final String NORMAL_COLOR = "#536DFE";
    public static final String[] COLOR_1 = new String[]{
            "#A992FE",
            "#FDDF4E",
            "#5EEFA0",
            "#FFA17D",
            "#D68DF9",
            "#86B0FC",
            "#FC9586",
            "#65BFEA",
            "#659DEA",
            "#6A65EA",
            "#A97CF8",
            "#E891F3",
            "#F391D5",
            "#F391B4",
            "#F39193",
            "#96C487",
            "#87C2C4"
    };
    public static final String ACCOUNT = "ACCOUNT";
    public static final String PASSWORD = "PASSWORD";

    public static final int MAX_SCHEDULE_LIMIT = 5;
    public static final String APP_WIDGET_ID = "app_widget_id";
    public static final String NEXT_DAY_STATUS = "NEXT_DAY_STATUS_";
    public static final String ADD_NORMAL_TIMETABLE = "add_normal_timetable";
    public static final String EGG = "EGG";

    //强制最大周
    public static int MAX_WEEK = 30;
    public static int STAND_SESSION = 12;
    public static int MAX_SESSION = 20;


    public static final String FILE_SELECTOR_DONT_TIP = "file_selector_dont_tip";
}
