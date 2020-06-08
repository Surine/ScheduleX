package cn.surine.schedulex.app_widget.normal_week;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

import cn.surine.coursetableview.entity.BTimeTable;
import cn.surine.schedulex.base.controller.App;
import cn.surine.schedulex.base.utils.Uis;
import cn.surine.schedulex.data.entity.Course;
import cn.surine.schedulex.data.helper.DataHandler;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/8 16:25
 */
public class BitmapMaker {

    //创建当日图
    public static Bitmap createWeekBitmap(PaintConfig pc, List<Course> courseList, BTimeTable timeTable, int maxSession) {
        int leftPadding = Uis.dip2px(App.context, 15);
        int rightPadding = Uis.dip2px(App.context, 15);
        int slotHeight = Uis.dip2px(App.context,90);
        //将所有信息绘制在画布上
        Bitmap bitmap = Bitmap.createBitmap(Uis.getScreenWidth() - Uis.dip2px(App.context, 10), maxSession * slotHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        //侧边栏
        Paint pSlide = new Paint(Paint.ANTI_ALIAS_FLAG);
        pSlide.setTextSize(Uis.sp2px(15));
        for (int i = 0; i < maxSession; i++) {
            canvas.drawText(String.valueOf(i + 1), 50, 50 + slotHeight * i, pSlide);
            pSlide.setTextSize(Uis.sp2px(10));
            try {
                canvas.drawText(timeTable.timeInfoList.get(i).startTime, 25, 80 + slotHeight * i, pSlide);
                canvas.drawText(timeTable.timeInfoList.get(i).endTime, 25, 110 + slotHeight * i, pSlide);
            } catch (Exception ignored) {

            }
            pSlide.setTextSize(Uis.sp2px(15));
        }

        //课程
        Paint pCourse = new Paint(Paint.ANTI_ALIAS_FLAG);
        pCourse.setColor(Color.GREEN);
        int tinySlotWidth = Uis.dip2px(App.context,3);
        int tinySlotHeight = Uis.dip2px(App.context,3);
        int slotWidth = (bitmap.getWidth() - leftPadding-rightPadding - (tinySlotWidth * 7)) / 8;
        int lastIndexWidth = slotWidth;
        for (int i = 1; i <= 7; i++) {
            List<Course> courses = DataHandler.getOneDayCourse(i, courseList);
            for (int j = 0; j < courses.size(); j++) {
                Course course = courses.get(j);
                pCourse.setColor(Color.parseColor(course.color));
                RectF rectF = new RectF(lastIndexWidth, (Integer.parseInt(course.classSessions) - 1) * slotHeight, lastIndexWidth + slotWidth, (Integer.parseInt(course.classSessions) - 1 + Integer.parseInt(course.continuingSession)) * slotHeight-tinySlotHeight);
                canvas.drawRoundRect(rectF, 20, 20, pCourse);
            }
            lastIndexWidth += (slotWidth + tinySlotWidth);
        }

        return bitmap;
    }
}
