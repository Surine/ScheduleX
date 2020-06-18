package cn.surine.schedulex.base;

import cn.surine.schedulex.data.entity.Palette;

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


    public static final String[] GRASS = new String[]{
            "#8ACF7E",
            "#B4D32D",
            "#7ABF10",
            "#88B846",
            "#6C9431",
            "#BCD085",
            "#305418",
    };

    public static final String[] DIOR = new String[]{
            "#FC0404",
            "#AC1C34",
            "#DC0404",
            "#C4211B",
            "#D71F24",
            "#AC1C2C",
            "#FC3C04",
    };

    public static final String[] DARK_SKY = new String[]{
        "#182020",
        "#202828",
        "#101818",
        "#203028",
        "#000000",
        "#606868",
        "#384848",
    };

    public static final String[] CYBER = new String[]{
         "#783860",
         "#40A0D8",
         "#1068A8",
         "#58D0F0",
         "#A03880",
         "#D89898",
         "#B86080",
         "#183050",
    };

    public static final String[] COLD = new String[]{
          "#CFD8DC",
          "#90AEA4",
          "#78909C",
          "#455A64",
          "#263238",
          "#607E8B",
          "#416F78",
          "#245A64",
    };


    public static final String[] GIRL = new String[]{
        "#FCE4EC",
        "#F8BBD0",
        "#F06292",
        "#EC407A",
        "#D81B60",
        "#880E4F",
        "#FF4081",
        "#FF80AB",
        "#C51162",
    };

    private static final String[] RAINBOW = new String[]{
           "#E95759",
           "#FC9638",
           "#F8D62F",
           "#61BF5C",
           "#27E8BF",
           "#70A9FE",
           "#B36EFE",
           "#EFAB26",
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

    public static final Palette p1 = new Palette(0, "", COLOR_1, "默认");
    public static final Palette p2 = new Palette(1, "", GRASS, "草地");
    public static final Palette p3 = new Palette(2, "", DIOR, "口红");
    public static final Palette p4 = new Palette(3, "", DARK_SKY, "深夜");
    public static final Palette p5 = new Palette(4, "", CYBER, "CyberPunk");
    public static final Palette p6 = new Palette(5, "", COLD, "冷淡");
    public static final Palette p7 = new Palette(6, "", GIRL, "少女");
    public static final Palette p8 = new Palette(7, "", RAINBOW, "彩虹");

}
