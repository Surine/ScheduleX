package cn.surine.schedulex.ui.view.custom.helper

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.utils.Uis

class ItemDecoration : RecyclerView.ItemDecoration() {
    private val divideHeight = Uis.dip2px(App.context, 2f)
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4F
        color = App.context.resources.getColor(R.color.gray_light)
        pathEffect = DashPathEffect(floatArrayOf(20F, 10F), 10F)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        val left = Uis.dip2px(App.context, 26f)
        val right = parent.width - Uis.dip2px(App.context, 26f)
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom
            c.drawPath(Path().apply {
                moveTo(left.toFloat(), top.toFloat())
                lineTo(right.toFloat(), top.toFloat())
            }, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = divideHeight
    }

}