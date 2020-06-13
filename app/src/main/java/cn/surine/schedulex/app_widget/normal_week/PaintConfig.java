package cn.surine.schedulex.app_widget.normal_week;

import android.graphics.Color;

import cn.surine.schedulex.data.helper.DataHandler;

/**
 * Intro：
 * 周视图绘制配置项
 *
 * @author sunliwei
 * @date 2020/6/8 17:32
 */
public class PaintConfig {
    public int slotHeight = DataHandler.abt.getInstance().getCurSchedule().itemHeight;
    public int itemRadius = 3;
    public int gapHeight = 3;
    public int gapWidth = 3;
    public int courseTextColor = Color.WHITE;
    public float itemStroke = 1.2F;
}
