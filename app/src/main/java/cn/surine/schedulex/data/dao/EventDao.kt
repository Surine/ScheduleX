package cn.surine.schedulex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import cn.surine.schedulex.data.entity.Event

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/29/20 17:11
 */
@Dao
interface EventDao{
    @Insert
    fun insert(event: Event): Long
}