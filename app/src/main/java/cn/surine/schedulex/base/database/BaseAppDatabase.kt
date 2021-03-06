package cn.surine.schedulex.base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.surine.schedulex.base.Constants
import cn.surine.schedulex.data.dao.CourseDao
import cn.surine.schedulex.data.dao.ScheduleDao
import cn.surine.schedulex.data.dao.TimeTableDao
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.entity.Schedule
import cn.surine.schedulex.data.entity.TimeTable

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:53
 */
@Database(entities = [Course::class, Schedule::class, TimeTable::class], version = 4)
abstract class BaseAppDatabase : RoomDatabase() {
    /**
     * 获取课表数据DAO
     */
    abstract fun courseDao(): CourseDao?

    abstract fun scheduleDao(): ScheduleDao?
    abstract fun timeTableDao(): TimeTableDao?

    companion object {
        @Volatile
        private var instance: BaseAppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): BaseAppDatabase? {
            if (instance == null) {
                synchronized(BaseAppDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(context.applicationContext, BaseAppDatabase::class.java, Constants.DB_NAME)
                                .allowMainThreadQueries() //TODO：slw 主线程访问，不安全
                                .addMigrations(mg_1_2, mg_2_3, mg_3_4)
                                .build()
                    }
                }
            }
            return instance
        }

        /**
         * 课程表支持显示周末和课程格子透明度
         */
        val mg_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE schedule ADD COLUMN isShowWeekend INTEGER NOT NULL DEFAULT 0 ")
                database.execSQL("ALTER TABLE schedule ADD COLUMN alphaForCourseItem INTEGER NOT NULL DEFAULT 10 ")
            }
        }
        /**
         * 课程表支持设置最大节次和item高度
         * 课程表支持显示导入方式
         * 新加入时间表
         * 课表支持时间显示
         */
        val mg_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE schedule ADD COLUMN maxSession INTEGER NOT NULL DEFAULT 12")
                database.execSQL("ALTER TABLE schedule ADD COLUMN itemHeight INTEGER NOT NULL DEFAULT 60 ")
                database.execSQL("ALTER TABLE schedule ADD COLUMN importWay INTEGER NOT NULL DEFAULT 0")
                database.execSQL("CREATE TABLE IF NOT EXISTS timetable (roomId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT,startTime INTEGER NOT NULL DEFAULT 0,rule TEXT)")
                database.execSQL("ALTER TABLE schedule ADD COLUMN timeTableId INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE schedule ADD COLUMN isShowTime INTEGER NOT NULL DEFAULT 1 ")
            }
        }
        /**
         * 加入课程主题
         */
        val mg_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE schedule ADD COLUMN courseThemeId INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}