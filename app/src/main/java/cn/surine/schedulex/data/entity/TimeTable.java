package cn.surine.schedulex.data.entity;

/**
 * Intro：
 * 时间表
 *
 * @author sunliwei
 * @date 2020-02-11 17:55
 */
public class TimeTable extends BaseVm {

    /**
     * 课时，一般为45分钟，或者40分钟
     */
    private int classHour;


    /**
     * 早上，下午，晚上开始上课时间
     */
    private String amTime;
    private String pmTime;
    private String nightTime;

    /**
     * 默认课时，后期需要兼容的话再改
     */
    private int amClassHour = 4;
    private int pmClassHour = 4;
    private int nightClassHour = 4;

    /**
     * 课时间隔
     */
    private int classHourGap;

    /**
     * 课间隔
     */
    private int classGap;

}
