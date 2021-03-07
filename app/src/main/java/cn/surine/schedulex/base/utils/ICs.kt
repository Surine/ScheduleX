package cn.surine.schedulex.base.utils

import android.content.Context
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.Dur
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.component.VAlarm
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VTimeZone
import net.fortuna.ical4j.model.property.*
import net.fortuna.ical4j.util.UidGenerator
import java.io.FileOutputStream

/**
 * 导出为ical文件
 * */
object ICs {
    private val tz: VTimeZone = TimeZoneRegistryFactory.getInstance().createRegistry().getTimeZone("Asia/Shanghai").vTimeZone

    fun getVEvent(context: Context, title: String, description: String, startTime: Long, endTime: Long, remindTime: Int = 15, addr:String = ""): VEvent {
        val summary = title
        // 开始时间
        val start = DateTime(startTime)
        start.isUtc = true
        // 结束时间
        val end = DateTime(endTime)
        end.isUtc = true
        // 新建普通事件
        val event = VEvent(start, end, summary)
        event.properties.add(Location(addr))
        // 生成唯一标示
        event.properties.add(Uid(UidGenerator("iCal4j").generateUid().getValue()))
        // 添加时区信息
        event.properties.add(tz.timeZoneId)
        // 提醒,提前10分钟
        val valarm = VAlarm(Dur(0, 0, -1 * remindTime, 0))
        valarm.properties.add(Summary("闹钟"))
        valarm.properties.add(Action.DISPLAY)
        valarm.properties.add(Description("请不要忘记上课哦～"))
        // 将VAlarm加入VEvent
        event.alarms.add(valarm)
        return event
    }


    fun exportIcs(events:List<VEvent>){
        // 创建日历
        val calendar = Calendar()
        calendar.properties.add(ProdId("SCHEDULEX-COURSES"))
        calendar.properties.add(Version.VERSION_2_0)
        calendar.properties.add(CalScale.GREGORIAN)
        // 添加事件
        events.forEach {
            calendar.components.add(it)
        }
        // 验证
        calendar.validate()
        val fout = FileOutputStream(Files.DOWNLOAD_FILE + "/${System.currentTimeMillis()}_courses.ics")
        val outputter = CalendarOutputter()
        outputter.output(calendar, fout)
    }
}