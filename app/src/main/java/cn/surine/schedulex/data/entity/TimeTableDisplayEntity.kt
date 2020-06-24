package cn.surine.schedulex.data.entity

class TimeTableDisplayEntity {
    @JvmField
    var session = 0
    @JvmField
    var startTime: String? = null
    @JvmField
    var endTime: String? = null

    constructor(session: Int, startTime: String?, endTime: String?) {
        this.session = session
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor() {}

    fun displayTime(): String {
        return "$startTime-$endTime"
    }

    fun displaySession(): String {
        return "第" + session + "节"
    }

    fun setSession(session: Int): TimeTableDisplayEntity {
        this.session = session
        return this
    }

    fun setStartTime(startTime: String?): TimeTableDisplayEntity {
        this.startTime = startTime
        return this
    }

    fun setEndTime(endTime: String?): TimeTableDisplayEntity {
        this.endTime = endTime
        return this
    }
}