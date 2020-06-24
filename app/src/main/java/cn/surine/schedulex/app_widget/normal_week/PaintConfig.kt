package cn.surine.schedulex.app_widget.normal_week

import android.graphics.Color
import cn.surine.schedulex.data.helper.DataHandler

/**
 * Intro：
 * 周视图绘制配置项
 *
 * @author sunliwei
 * @date 2020/6/8 17:32
 */
class PaintConfig {
    var slotHeight: Int = DataHandler.getCurSchedule().itemHeight
    var itemRadius = 3
    var gapHeight = 3
    var gapWidth = 3
    var courseTextColor = Color.WHITE
    var itemStroke = 1.2f
    var isShowWeek: Boolean = DataHandler.getCurSchedule().isShowWeekend
}