package cn.surine.coursetableview.view;

import android.graphics.Color;

/**
 * Created by Surine on 2019/2/26.
 * 课程表UI属性配置
 */

public class UIConfig {

    /*默认数据*/
    private final int NORMAL_MAX_SECTION = 30;
    private final int NORMAL_MAX_CLASS_DAY = 7;
    private final int NORMAL_SECTION_HEIGHT = 200;
    private final int NORMAL_CHOOSE_WEEK_COLOR = Color.RED;
    private final float NORMAL_ITEM_CORNER_RADIUS = 5;
    public static final String[] WEEK_INFO_ONE = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public static final String[] WEEK_INFO_TWO = {"一", "二", "三", "四", "五", "六", "日"};
    private final int NORMAL_ITEM_TOP_MARGIN = 3;
    private final boolean NORMAL_SHOW_CUR_WEEK_COURSE = true;
    private final int NORMAL_COURSE_TEXT_SIZE = 12;
    private final int NORMAL_NOT_CUR_WEEK_COURSE_COLOR = Color.argb(20, 0, 0, 0);
    private final int NORMAL_SECTION_VIEW_WIDTH = 70;
    private final int NORMAL_ITEM_SIDE_MARGIN = 4;
    public static final int LIGHT = Color.WHITE;  //白色UI
    public static final int DARK = Color.BLACK;   //黑色UI

    /*最大上课节次 ，默认12*/
    private int maxSection = NORMAL_MAX_SECTION;
    /*最大上课天，默认7，可设置为5*/
    private int maxClassDay = NORMAL_MAX_CLASS_DAY;
    /*最大课程高度*/
    private int sectionHeight = NORMAL_SECTION_HEIGHT;
    /*weekinfo集合*/
    private String[] weekInfoStyle = WEEK_INFO_TWO;
    /*选中项（当前日是周几）的颜色，默认为红*/
    private int chooseWeekColor = NORMAL_CHOOSE_WEEK_COLOR;
    /*item项圆角*/
    private float itemCornerRadius = NORMAL_ITEM_CORNER_RADIUS;
    /*课程项距离 top margin*/
    private int itemTopMargin = NORMAL_ITEM_TOP_MARGIN;
    /*是否显示非本周上的课*/
    private boolean showCurWeekCourse = NORMAL_SHOW_CUR_WEEK_COURSE;
    /*课程项字体大小*/
    private int itemTextSize = NORMAL_COURSE_TEXT_SIZE;
    /*非本周上的课的课程颜色*/
    private int itemNotCurWeekCourseColor = NORMAL_NOT_CUR_WEEK_COURSE_COLOR;
    /*侧边栏宽度*/
    private int sectionViewWidth = NORMAL_SECTION_VIEW_WIDTH;
    /*item侧边距*/
    private int itemSideMargin = NORMAL_ITEM_SIDE_MARGIN;
    /*文字UI*/
    private int colorUI = DARK;
    /**
     * 是否显示时间表
     */
    private boolean isShowTimeTable = true;

    public int getColorUI() {
        return colorUI;
    }

    /**
     * 设置UI
     *
     * @param colorUI UI，两种，DARK和LIGHT
     * @return UIConfig
     */
    public UIConfig setColorUI(int colorUI) {
        if (colorUI == DARK || colorUI == LIGHT) {
            this.colorUI = colorUI;
        }
        return this;
    }

    public int getItemSideMargin() {
        return itemSideMargin;
    }


    /**
     * 设置侧边距
     *
     * @param itemSideMargin 测边距 0 <= a <= 40 单位px，默认4
     * @return UIConfig
     */
    public UIConfig setItemSideMargin(int itemSideMargin) {
        if (itemSideMargin >= 0 && itemSideMargin <= 40) {
            this.itemSideMargin = itemSideMargin;
        }
        return this;
    }

    public int getSectionViewWidth() {
        return sectionViewWidth;
    }


    /**
     * 设置侧边栏宽度
     *
     * @param sectionViewWidth 宽度 20 <= a <= 100 单位px，默认70
     * @return UIConfig
     */
    public UIConfig setSectionViewWidth(int sectionViewWidth) {
        if (sectionViewWidth >= 20 && sectionViewWidth <= 200) {
            this.sectionViewWidth = sectionViewWidth;
        }
        return this;
    }

    public int getItemNotCurWeekCourseColor() {
        return itemNotCurWeekCourseColor;
    }

    /**
     * 非本周上的课的课程背景颜色
     *
     * @param itemNotCurWeekCourseColor 背景颜色
     * @return UIConfig
     */
    public UIConfig setItemNotCurWeekCourseColor(int itemNotCurWeekCourseColor) {
        this.itemNotCurWeekCourseColor = itemNotCurWeekCourseColor;
        return this;
    }

    public int getItemTextSize() {
        return itemTextSize;
    }

    /**
     * 设置课程项字体大小
     *
     * @param itemTextSize 字体大小，单位sp,范围5 <= a <= 18
     * @return UIConfig
     */
    public UIConfig setItemTextSize(int itemTextSize) {
        if (itemTextSize >= 5 && itemTextSize <= 18) {
            this.itemTextSize = itemTextSize;
        }
        return this;
    }


    public boolean isShowCurWeekCourse() {
        return showCurWeekCourse;
    }

    /**
     * 是否显示非本周上的课
     *
     * @param showCurWeekCourse true为显示，false为不显示 默认显示
     * @return UIConfig
     */
    public UIConfig setShowCurWeekCourse(boolean showCurWeekCourse) {
        this.showCurWeekCourse = showCurWeekCourse;
        return this;
    }

    public int getItemTopMargin() {
        return itemTopMargin;
    }


    /**
     * 设置item top margin
     *
     * @param itemTopMargin margin值，单位px，必须大于等于0
     */
    public UIConfig setItemTopMargin(int itemTopMargin) {
        if (itemTopMargin >= 0) {
            this.itemTopMargin = itemTopMargin;
        }
        return this;
    }

    public String[] getWeekInfoStyle() {
        return weekInfoStyle;
    }


    /**
     * 设置weekInfoStyle
     *
     * @param weekInfoStyle 传入的一个参数集合,必须为长度为7
     */
    public UIConfig setWeekInfoStyle(String[] weekInfoStyle) {
        //限制最大7 classDay
        if (weekInfoStyle.length == NORMAL_MAX_CLASS_DAY) {
            this.weekInfoStyle = weekInfoStyle;
        }
        return this;
    }

    public int getMaxSection() {
        return maxSection;
    }

    /**
     * 设置最大上课节次
     *
     * @param maxSection 最大上课节次
     */
    public UIConfig setMaxSection(int maxSection) {
        //限制最大 section
        if (maxSection <= NORMAL_MAX_SECTION) {
            this.maxSection = maxSection;
        }
        return this;
    }

    public int getMaxClassDay() {
        return maxClassDay;
    }


    /**
     * 设置一周最大上课天数（即是否显示周末）
     *
     * @param maxClassDay 一周最大上课天数 (只能设置5和7)
     */
    public UIConfig setMaxClassDay(int maxClassDay) {
        if (maxClassDay == 5 || maxClassDay == NORMAL_MAX_CLASS_DAY) {
            this.maxClassDay = maxClassDay;
        }
        return this;
    }


    public int getSectionHeight() {
        return sectionHeight;
    }


    /**
     * 设置小课课程最大高度
     *
     * @param sectionHeight 小课课程最大高度
     */
    public UIConfig setSectionHeight(int sectionHeight) {
        //暂时限制宽高
        if (sectionHeight >= 30 && sectionHeight < 500) {
            this.sectionHeight = sectionHeight;
        }
        return this;
    }


    public int getChooseWeekColor() {
        return chooseWeekColor;
    }


    /**
     * 设置选中项（当前日是周几）的颜色
     *
     * @param chooseWeekColor 颜色值
     */
    public UIConfig setChooseWeekColor(int chooseWeekColor) {
        this.chooseWeekColor = chooseWeekColor;
        return this;
    }

    public float getItemCornerRadius() {
        return itemCornerRadius;
    }

    /**
     * 设置item项圆角
     *
     * @param itemCornerRadius 圆角
     */
    public UIConfig setItemCornerRadius(float itemCornerRadius) {
        if (itemCornerRadius >= 0 && itemCornerRadius <= 360) {
            this.itemCornerRadius = itemCornerRadius;
        }
        return this;
    }

    public boolean isShowTimeTable() {
        return isShowTimeTable;
    }

    public void setShowTimeTable(boolean showTimeTable) {
        isShowTimeTable = showTimeTable;
    }
}
