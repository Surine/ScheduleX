package cn.surine.schedulex.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/28/20 16:05
 */
@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var text: String = "",
        var position: String = "",
        var time: Long = 0,
        var type: Int = 0,
        var color: String = "",
        var extra: String = ""
)

@Entity
data class Event(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        var done: Boolean = false,
        var content: String = "",
        var parentId: Long = 0
)


//fix: There is a problem with the query: [SQLITE_ERROR] SQL error or missing database (no such table: Event)
//不要忘记在AppDatabase注册类（table）
data class MemoWithEvent(
        @Embedded var memo: Memo,
        @Relation(
                parentColumn = "id",
                entityColumn = "parentId") var events: List<Event>
)
