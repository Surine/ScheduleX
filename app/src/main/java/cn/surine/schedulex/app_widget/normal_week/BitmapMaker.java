package cn.surine.schedulex.app_widget.normal_week;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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
        //默认左右边距
        int leftPadding = Uis.dip2px(App.context, 5);
        int rightPadding = Uis.dip2px(App.context, 0);
        //侧边栏宽度
        int slideWidth = Uis.dip2px(App.context, 30);
        //注意这里的slotHeight代表了，每个单位的高度，其中包含了一个垂直方向上的间隙
        int slotHeight = Uis.dip2px(App.context, pc.slotHeight);
        //间隙
        int gapSlotWidth = Uis.dip2px(App.context, pc.gapWidth);
        int gapSlotHeight = Uis.dip2px(App.context, pc.gapHeight);
        //圆角
        int itemRadius = Uis.dip2px(App.context, pc.itemRadius);
        //边框
        int itemStroke = Uis.dip2px(App.context, pc.itemStroke);
        //画布宽不会很精确，小部件有侧边，高度就是最大的高度 * 最大节次
        Bitmap bitmap = Bitmap.createBitmap(Uis.getScreenWidth() - Uis.dip2px(App.context, 10), maxSession * slotHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        //绘制侧边栏
        Paint pSlide = new Paint(Paint.ANTI_ALIAS_FLAG);
        pSlide.setTextSize(Uis.sp2px(15));
        int endX = leftPadding + slideWidth;
        Paint.FontMetrics fontMetrics = pSlide.getFontMetrics();
        for (int i = 0; i < maxSession; i++) {
            //测量节次文字，并绘制
            float textWidth = pSlide.measureText(String.valueOf(i + 1));
            float offSet = pSlide.getFontSpacing() / 2 - (fontMetrics.descent - fontMetrics.leading) - 10;
            canvas.drawText(String.valueOf(i + 1), leftPadding + (endX - leftPadding) / 2F - textWidth / 2F, slotHeight / 2F - offSet + slotHeight * i, pSlide);
            pSlide.setTextSize(Uis.sp2px(9));
            try {
                //测量开始结束时间并绘制
                float startTimeTextWidth = pSlide.measureText(timeTable.timeInfoList.get(i).startTime);
                canvas.drawText(timeTable.timeInfoList.get(i).startTime, leftPadding + (endX - leftPadding) / 2F - startTimeTextWidth / 2F, slotHeight / 2F - offSet + 30 + slotHeight * i, pSlide);
                float endTimeTextWidth = pSlide.measureText(timeTable.timeInfoList.get(i).startTime);
                canvas.drawText(timeTable.timeInfoList.get(i).endTime, leftPadding + (endX - leftPadding) / 2F - endTimeTextWidth / 2F, slotHeight / 2F - offSet + 60 + slotHeight * i, pSlide);
            } catch (Exception ignored) {

            }
            pSlide.setTextSize(Uis.sp2px(15));
        }

        //课程
        Paint pCourse = new Paint(Paint.ANTI_ALIAS_FLAG);
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(Uis.sp2px(11.3F));
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.WHITE);
        int slotWidth = (bitmap.getWidth() - leftPadding - rightPadding - slideWidth - (gapSlotWidth * 6)) / 7;
        int lastIndexWidth = slideWidth + leftPadding + 6; //微调起始
        for (int i = 1; i <= 7; i++) {
            List<Course> courses = DataHandler.getOneDayCourse(i, courseList);
            for (int j = 0; j < courses.size(); j++) {
                Course course = courses.get(j);
                //背景框
                pCourse.setColor(Color.WHITE);
                RectF rectF = new RectF(lastIndexWidth, (Integer.parseInt(course.classSessions) - 1) * slotHeight, lastIndexWidth + slotWidth, (Integer.parseInt(course.classSessions) - 1 + Integer.parseInt(course.continuingSession)) * slotHeight - gapSlotHeight);
                canvas.drawRoundRect(rectF, itemRadius, itemRadius, pCourse);

                //主色框
                pCourse.setColor(Color.parseColor(course.color));
                RectF rectFWidth = new RectF(rectF.left + itemStroke, rectF.top + itemStroke, rectF.right - itemStroke, rectF.bottom - itemStroke);
                canvas.drawRoundRect(rectFWidth, itemRadius, itemRadius, pCourse);

                //文字
                canvas.save();
                //间距将微调
                int offset = 10;
                //下面是一个防止文本内容溢出的写法
                StaticLayout sl = null;
                int len = 6;
                do {
                    String data = course.coureName.substring(0, Math.min(course.coureName.length(), len--)) + "..@" + course.teachingBuildingName + course.classroomName;
                    sl = new StaticLayout(data, textPaint, slotWidth - 2 * itemStroke - offset, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                } while (sl.getHeight() > rectFWidth.height() && len >= 2);
                canvas.translate(lastIndexWidth + itemStroke + offset, (Integer.parseInt(course.classSessions) - 1) * slotHeight + itemStroke + offset);
                sl.draw(canvas);
                canvas.restore();
            }
            lastIndexWidth += (slotWidth + gapSlotWidth);
        }
        return bitmap;
    }
}
