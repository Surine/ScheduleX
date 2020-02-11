package cn.surine.schedulex.base.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.TimeZone;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.controller.App;

/**
 * Intro：
 * 实现日历的添加
 *
 * @author sunliwei
 * @date 2020-02-09 09:35
 */

@SuppressLint("MissingPermission")
public class CalendarProviders {

    private static Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
    private static Uri eventUri = CalendarContract.Events.CONTENT_URI;
    private static Uri reminderUri = CalendarContract.Reminders.CONTENT_URI;

    private static ContentResolver contentResolver;


    /**
     * 获取日历
     */
    private static int getCalendar() {
         Cursor cursor = contentResolver.query(calendarUri, null, null, null, null);
        if (Objs.isNull(cursor) || cursor.getCount() == 0) {
            return -1;
        } else {
            try {
                cursor.moveToFirst();
                //获取第一行的属性值，第一个日历的ID
                return cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
            } finally {
                cursor.close();
            }
        }
    }


    /**
     * 添加一个日历
     * */
    private static long addCalendar(){
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        String appName = App.context.getString(R.string.app_name);
        value.put(CalendarContract.Calendars.NAME, appName + "的日程");
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, appName);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, appName);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, appName);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, appName);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);
        value.put(CalendarContract.CALLER_IS_SYNCADAPTER,true);

        Uri insertResult = contentResolver.insert(calendarUri,value);
        if(insertResult == null){
            return -1;
        }else{
            return ContentUris.parseId(insertResult);
        }
    }



    private static void deleteCalendar(){

    }


    /**
     * 添加事件
     * */
    public static boolean addEvent(Context context,String title,String msg,long startTime,long endTime){
        contentResolver = context.getContentResolver();
        int calendarId = getCalendar();
        if(calendarId == -1){
            addCalendar();
            calendarId = getCalendar();
        }

        Calendar mCalendar = Calendar.getInstance();
        //设置开始时间
        mCalendar.setTimeInMillis(startTime);
        long start = mCalendar.getTime().getTime();
        //设置终止时间
        mCalendar.setTimeInMillis(endTime);
        long end = mCalendar.getTime().getTime();


        // 准备event
        ContentValues valueEvent = new ContentValues();
        valueEvent.put(CalendarContract.Events.DTSTART,start);
        valueEvent.put(CalendarContract.Events.DTEND,end);
        valueEvent.put(CalendarContract.Events.TITLE,title);
        valueEvent.put(CalendarContract.Events.DESCRIPTION,msg);
        valueEvent.put(CalendarContract.Events.CALENDAR_ID,calendarId);
        valueEvent.put(CalendarContract.Events.EVENT_TIMEZONE,"Asia/Shanghai");

        // 添加event
        Uri insertEventUri = contentResolver.insert(eventUri,valueEvent);
        if (insertEventUri == null){
            return false;
        }

        //添加提醒
        long eventId = ContentUris.parseId(insertEventUri);
        ContentValues valueReminder = new ContentValues();
        valueReminder.put(CalendarContract.Reminders.EVENT_ID,eventId);
        valueReminder.put(CalendarContract.Reminders.MINUTES,15);
        valueReminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);
        contentResolver.insert(reminderUri,valueReminder);

        return true;
    }



    /**
     * 删除日历事件
     * @param context 上下文
     * @param title 事件标题
     */
    public static void deleteCalendarEvent(@NonNull Context context, String title) {
        contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(eventUri, null, null, null, null);
        try {
            if (cursor == null) {
                return;
            }
            if (cursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String eventTitle = cursor.getString(cursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        //查询到名字一致的话， 就拼接id后进行删除
                        int id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                        Uri deleteUri = ContentUris.withAppendedId(eventUri, id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) {
                            return;
                        }
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
