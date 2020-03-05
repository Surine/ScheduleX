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
     * 起始 如8:00
     */
    private int startTime;

    /**
     * 规则
     * {"45","10","45","10","45" ……}
     * */
    private String rule;

}
