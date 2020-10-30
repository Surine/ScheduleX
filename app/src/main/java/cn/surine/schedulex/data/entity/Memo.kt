package cn.surine.schedulex.data.entity

import androidx.room.*
import cn.surine.schedulex.base.utils.Dates
import java.util.*

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/28/20 16:05
 */
@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var text: String = "",
        var position: String = "",
        var time: Long = 0,
        var type: Int = 0,
        var color: String = "",
        var extra: String = "",
        var afterNote: String = "",
        @Ignore var childEvent: MutableList<Event> = mutableListOf()
) {
    val displayTime: String
        get() {
            val curTime = System.currentTimeMillis()
            val offset = time - curTime
            return when {
                offset < 0 -> {
                    "Before ${offset / Dates.ONE_DAY} day"
                }
                offset > 10 -> {
                    Dates.getDateFormat(Date(time), Dates.yyyyMMddHHmm)
                }
                offset < Dates.ONE_HOUR -> {
                    "${offset / Dates.ONE_HOUR * 1F * 60}"
                }
                else -> {
                    "After ${offset / Dates.ONE_DAY} day ${Dates.getDateFormat(Date(time), Dates.HHmm)}"
                }
            }
        }
}

@Entity
data class Event(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var done: Boolean = false,
        var content: String = "",
        var parentId: Long
)


//fix: There is a problem with the query: [SQLITE_ERROR] SQL error or missing database (no such table: Event)
//不要忘记在AppDatabase注册类（table）
data class MemoWithEvent(
        @Embedded val memo: Memo,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId") val events: List<Event>
)
