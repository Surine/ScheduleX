package cn.surine.schedulex.app_widget.normal_week

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import cn.surine.coursetableview.entity.BTimeTable
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.utils.Uis
import cn.surine.schedulex.data.entity.Course
import cn.surine.schedulex.data.helper.DataHandler

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/8 16:25
 */
object BitmapMaker {
    //创建当日图
    @JvmStatic
    fun createWeekBitmap(pc: PaintConfig, courseList: List<Course>, timeTable: BTimeTable, maxSession: Int): Bitmap { //默认左右边距
        val leftPadding = Uis.dip2px(App.context, 5f)
        val rightPadding = Uis.dip2px(App.context, 0f)
        //侧边栏宽度
        val slideWidth = Uis.dip2px(App.context, 30f)
        //注意这里的slotHeight代表了，每个单位的高度，其中包含了一个垂直方向上的间隙
        val slotHeight = Uis.dip2px(App.context, pc.slotHeight.toFloat())
        //间隙
        val gapSlotWidth = Uis.dip2px(App.context, pc.gapWidth.toFloat())
        val gapSlotHeight = Uis.dip2px(App.context, pc.gapHeight.toFloat())
        //圆角
        val itemRadius = Uis.dip2px(App.context, pc.itemRadius.toFloat())
        //边框
        val itemStroke = Uis.dip2px(App.context, pc.itemStroke)
        //画布宽不会很精确，小部件有侧边，高度就是最大的高度 * 最大节次
        val bitmap = Bitmap.createBitmap(Uis.getScreenWidth() - Uis.dip2px(App.context, 10f), maxSession * slotHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        //绘制侧边栏
        val pSlide = Paint(Paint.ANTI_ALIAS_FLAG)
        pSlide.textSize = Uis.sp2px(15f)
        pSlide.color = if (pc.mainUiColor) Color.WHITE else Color.BLACK
        val endX = leftPadding + slideWidth
        val fontMetrics = pSlide.fontMetrics
        for (i in 0 until maxSession) { //测量节次文字，并绘制
            val textWidth = pSlide.measureText((i + 1).toString())
            val offSet = pSlide.fontSpacing / 2 - (fontMetrics.descent - fontMetrics.leading) - 10
            canvas.drawText((i + 1).toString(), leftPadding + (endX - leftPadding) / 2f - textWidth / 2f, slotHeight / 2f - offSet + slotHeight * i, pSlide)
            pSlide.textSize = Uis.sp2px(9f)
            try { //测量开始结束时间并绘制
                val startTimeTextWidth = pSlide.measureText(timeTable.timeInfoList[i].startTime)
                canvas.drawText(timeTable.timeInfoList[i].startTime, leftPadding + (endX - leftPadding) / 2f - startTimeTextWidth / 2f, slotHeight / 2f - offSet + 30 + slotHeight * i, pSlide)
                val endTimeTextWidth = pSlide.measureText(timeTable.timeInfoList[i].startTime)
                canvas.drawText(timeTable.timeInfoList[i].endTime, leftPadding + (endX - leftPadding) / 2f - endTimeTextWidth / 2f, slotHeight / 2f - offSet + 60 + slotHeight * i, pSlide)
            } catch (ignored: Exception) {
            }
            pSlide.textSize = Uis.sp2px(15f)
        }
        //课程
        val pCourse = Paint(Paint.ANTI_ALIAS_FLAG)
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = Uis.sp2px(11.3f)
        textPaint.isFakeBoldText = true
        textPaint.color = Color.WHITE
        val slotWidth = (bitmap.width - leftPadding - rightPadding - slideWidth - gapSlotWidth * 6) / if (pc.isShowWeek) 7 else 5
        var lastIndexWidth = slideWidth + leftPadding + 6 //微调起始
        for (i in 1..if (pc.isShowWeek) 7 else 5) {
            val courses: List<Course> = DataHandler.getOneDayCourse(i, courseList)
            for (j in courses.indices) {
                val course = courses[j]
                //背景框
                pCourse.color = Color.WHITE
                val rectF = RectF(lastIndexWidth.toFloat(), ((course.classSessions.toInt() - 1) * slotHeight).toFloat(), (lastIndexWidth + slotWidth).toFloat(), ((course.classSessions.toInt() - 1 + course.continuingSession.toInt()) * slotHeight - gapSlotHeight).toFloat())
                canvas.drawRoundRect(rectF, itemRadius.toFloat(), itemRadius.toFloat(), pCourse)
                //主色框
                pCourse.color = Color.parseColor(course.color)
                val rectFWidth = RectF(rectF.left + itemStroke, rectF.top + itemStroke, rectF.right - itemStroke, rectF.bottom - itemStroke)
                canvas.drawRoundRect(rectFWidth, itemRadius.toFloat(), itemRadius.toFloat(), pCourse)
                //文字
                canvas.save()
                //间距将微调
                val offset = 10
                //下面是一个防止文本内容溢出的写法
                var sl: StaticLayout?
                var len = 6
                do {
                    val data = course.coureName.substring(0, Math.min(course.coureName.length, len--)) + "..@" + course.teachingBuildingName + course.classroomName
                    sl = StaticLayout(data, textPaint, slotWidth - 2 * itemStroke - offset, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)
                } while (sl!!.height > rectFWidth.height() && len >= 2)
                canvas.translate(lastIndexWidth + itemStroke + offset.toFloat(), (course.classSessions.toInt() - 1) * slotHeight + itemStroke + offset.toFloat())
                sl.draw(canvas)
                canvas.restore()
            }
            lastIndexWidth += slotWidth + gapSlotWidth
        }
        return bitmap
    }
}