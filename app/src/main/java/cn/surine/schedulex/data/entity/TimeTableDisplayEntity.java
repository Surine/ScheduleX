package cn.surine.schedulex.data.entity;

public class TimeTableDisplayEntity {
    public int session;
    public String startTime;
    public String endTime;

    public TimeTableDisplayEntity(int session, String startTime, String endTime) {
        this.session = session;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeTableDisplayEntity() {
    }

    public String displayTime() {
        return startTime + "-" + endTime;
    }

    public String displaySession() {
        return "第" + session + "节";
    }
}