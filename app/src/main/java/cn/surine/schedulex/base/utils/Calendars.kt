package cn.surine.schedulex.base.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import cn.surine.schedulex.R
import java.util.*

/**
 * Intro：
 *  日历工具类
 * @author sunliwei
 * @date 2020/6/30 11:48
 */
object Calendars {
    private val calendarUri = CalendarContract.Calendars.CONTENT_URI
    private val eventUri = CalendarContract.Events.CONTENT_URI
    private val reminderUri = CalendarContract.Reminders.CONTENT_URI


    /**
     * 检查系统中是否存在现有账户，存在返回账户id，否则返回-1
     * */
    @SuppressLint("MissingPermission")
    fun checkCalendarAccount(context: Context): Int {
        val cursor = context.contentResolver.query(calendarUri, null, null, null, null)
        return cursor.use {
            it?.moveToFirst()
            //下面会分别对应账户名和id，这里获取第一个
//           it?.getInt(it.getColumnIndex(CalendarContract.Calendars.NAME))
            it?.getInt(it.getColumnIndex(CalendarContract.Calendars._ID)) ?: -1
        }
    }


    /**
     * 添加一个日历
     */
    @SuppressLint("MissingPermission")
    private fun addCalendar(context: Context): Long {
        val timeZone = TimeZone.getDefault()
        val value = ContentValues()
        val appName = context.getString(R.string.app_name)
        value.put(CalendarContract.Calendars.NAME, appName + "的日程")
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, appName)
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, appName)
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, appName)
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, appName)
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        value.put(CalendarContract.CALLER_IS_SYNCADAPTER, true)
        val insertResult = context.contentResolver.insert(calendarUri, value)
        return if (insertResult == null) {
            -1
        } else {
            ContentUris.parseId(insertResult)
        }
    }

    /**
     * 添加日历事件
     * @param context
     * @param title 日程标题
     * @param description 日程内容
     * @param starTime 起始时间 时间戳
     * @param endTime 终止时间 时间戳
     * @param remindTime 提醒时间 分钟
     * */
    @SuppressLint("MissingPermission")
    fun addCalendarEvent(context: Context, title: String, description: String, startTime: Long, endTime: Long, remindTime: Int = 15): Long {
        //首先检查日历账户是否存在
        var calendarId = checkCalendarAccount(context)
        //不存在的话需要添加一个并重新获取账号id
        if (calendarId == -1) {
            addCalendar(context)
            calendarId = checkCalendarAccount(context)
        }


        val mCalendar = Calendar.getInstance()
        //设置开始时间
        mCalendar.timeInMillis = startTime
        val start = mCalendar.time.time
        //设置终止时间
        mCalendar.timeInMillis = endTime
        val end = mCalendar.time.time

        // 准备event
        val valueEvent = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, start) //起始时间
            put(CalendarContract.Events.DTEND, end) //终止时间
            put(CalendarContract.Events.TITLE, title) //标题
            put(CalendarContract.Events.DESCRIPTION, description) //描述
            put(CalendarContract.Events.CALENDAR_ID, calendarId)  //日历id
            put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai") //时区
            put(CalendarContract.Events.HAS_ALARM, 1) //闹钟提醒
        }

        // 添加event
        val insertEventUri = context.contentResolver.insert(eventUri, valueEvent)
                ?: return -1L

        //添加提醒
        val valueReminder = ContentValues()
        valueReminder.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(insertEventUri))
        valueReminder.put(CalendarContract.Reminders.MINUTES, remindTime)
        valueReminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM)
        context.contentResolver.insert(reminderUri, valueReminder)
        return ContentUris.parseId(insertEventUri)
    }


    /**
     * 清除与应用相关的事件
     * */
    @SuppressLint("MissingPermission")
    fun removeAllEvent(context: Context, did: Long) {
        context.contentResolver.query(eventUri, null, null, null, null).use { cursor ->
            cursor ?: return
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val mt = cursor.getLong(cursor.getColumnIndex("id"))
                    if (did == mt) {
                        //查询到一致的话， 就拼接id后进行删除
                        val id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                        val deleteUri = ContentUris.withAppendedId(eventUri, id.toLong())
                        val rows: Int = context.contentResolver.delete(deleteUri, null, null)
                        if (rows == -1) {
                            return
                        }
                    }
                    cursor.moveToNext()
                }
            }
        }
    }


    /**
     * 清除与应用相关的事件
     * */
    @SuppressLint("MissingPermission")
    fun removeAllEvent(context: Context, title: String) {
        context.contentResolver.query(eventUri, null, null, null, null).use { cursor ->
            cursor ?: return
            if (cursor.count > 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val mt = cursor.getString(cursor.getColumnIndex("title"))
                    if (title == mt) {
                        //查询到一致的话， 就拼接id后进行删除
                        val id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                        val deleteUri = ContentUris.withAppendedId(eventUri, id.toLong())
                        val rows: Int = context.contentResolver.delete(deleteUri, null, null)
                        if (rows == -1) {
                            return
                        }
                    }
                    cursor.moveToNext()
                }
            }
        }
    }
}