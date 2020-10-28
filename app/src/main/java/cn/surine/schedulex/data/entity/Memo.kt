package cn.surine.schedulex.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import cn.surine.schedulex.data.helper.EventConverters

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/28/20 16:05
 */
@Entity
@TypeConverters(EventConverters::class)
class Memo {
    @PrimaryKey
    var id = 0
    var text: String = ""
    var position: String = ""
    var time: Long = 0
    var type: Int = 0
    var color: String = ""
    var extra: String = ""
    var events:MutableList<Event> = mutableListOf()
}

class Event{
    var done:Boolean = false
    var content:String = ""
}