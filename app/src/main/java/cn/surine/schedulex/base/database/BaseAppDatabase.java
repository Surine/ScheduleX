package cn.surine.schedulex.base.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import cn.surine.schedulex.base.Constants;
import cn.surine.schedulex.data.dao.CourseDao;
import cn.surine.schedulex.data.dao.ScheduleDao;
import cn.surine.schedulex.data.dao.TimeTableDao;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.entity.Schedule;
import cn.surine.schedulex.data.entity.TimeTable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-16 20:53
 */
@Database(entities = {Course.class, Schedule.class, TimeTable.class}, version = 5)
public abstract class BaseAppDatabase extends RoomDatabase {
    private volatile static BaseAppDatabase instance;

    public static BaseAppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (BaseAppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), BaseAppDatabase.class, Constants.DB_NAME)
                            .allowMainThreadQueries()  //TODO：slw 主线程访问，不安全
                            .addMigrations(mg_1_2, mg_2_3, mg_3_4)
                            .build();
                }
            }
        }
        return instance;
    }


    /**
     * 课程表支持显示周末和课程格子透明度
     */
    static final Migration mg_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE schedule ADD COLUMN isShowWeekend INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE schedule ADD COLUMN alphaForCourseItem INTEGER NOT NULL DEFAULT 10 ");
        }
    };


    /**
     * 课程表支持设置最大节次和item高度
     */
    static final Migration mg_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE schedule ADD COLUMN maxSession INTEGER NOT NULL DEFAULT 12");
            database.execSQL("ALTER TABLE schedule ADD COLUMN itemHeight INTEGER NOT NULL DEFAULT 60 ");
        }
    };


    /**
     * 课程表支持显示导入方式
     * 新加入时间表
     * 课表支持时间显示
     */
    static final Migration mg_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE schedule ADD COLUMN importWay INTEGER NOT NULL DEFAULT 0");
            database.execSQL("CREATE TABLE IF NOT EXISTS timetable (roomId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT,startTime INTEGER NOT NULL DEFAULT 0,rule TEXT)");
            database.execSQL("ALTER TABLE schedule ADD COLUMN timeTableId INTEGER NOT NULL DEFAULT 1");
        }
    };


    /**
     * 获取课表数据DAO
     */
    public abstract CourseDao courseDao();


    public abstract ScheduleDao scheduleDao();

    public abstract TimeTableDao timeTableDao();


}
