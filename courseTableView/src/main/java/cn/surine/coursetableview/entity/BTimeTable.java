package cn.surine.coursetableview.entity;

import java.util.List;

public class BTimeTable {
    public List<BTimeInfo> timeInfoList;

    public static class BTimeInfo {
        /**
         * 第几节
         */
        public int sessionNo;
        /**
         * 开始时间
         */
        public String startTime;
        /**
         * 结束时间
         */
        public String endTime;
    }
}
