package cn.surine.schedulex.data.helper

import androidx.room.TypeConverter
import cn.surine.schedulex.data.entity.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventConverters {
    @TypeConverter
    fun stringToObject(value: String): List<Event> {
        val listType = object : TypeToken<List<Event>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Event>): String {
        return Gson().toJson(list)
    }
}