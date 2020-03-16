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


    public TimeTableDisplayEntity setSession(int session) {
        this.session = session;
        return this;
    }

    public TimeTableDisplayEntity setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public TimeTableDisplayEntity setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }
}