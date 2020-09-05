package cn.surine.schedulex.base.utils

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import cn.surine.schedulex.R
import cn.surine.schedulex.base.controller.App

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 17:58
 */
object Imgs {
    @JvmStatic
    fun calculatePicLightValue(path: String): Int {
        var bitmap: Bitmap? = null
        var bitmapSmall: Bitmap? = null
        try {
            val a = System.currentTimeMillis()
            var light = 0.0
            bitmap = BitmapFactory.decodeFile(path)
            bitmapSmall = zoomImage(rawBitmap = bitmap, newWidth = bitmap?.width?.times(0.3)
                    ?: 0.0, newHeight = bitmap?.height?.times(0.3) ?: 0.0)
            for (i in 0 until bitmapSmall.width)
                for (j in 0 until bitmapSmall.height) {
                    val argb: Int = bitmapSmall.getPixel(i, j)
                    val r = argb shr 16 and 0xff
                    val g = argb shr 8 and 0xff
                    val b = argb and 0xff
                    light += 0.299 * r + 0.587 * g + 0.114 * b
                }
            val size = bitmapSmall.width * bitmapSmall.height
            return (light / (size)).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        } finally {
            bitmap?.recycle()
            bitmapSmall?.recycle()
        }
    }

    private fun zoomImage(
            rawBitmap: Bitmap, newWidth: Double,
            newHeight: Double
    ): Bitmap {
        val width = rawBitmap.width.toFloat()
        val height = rawBitmap.height.toFloat()
        val matrix = Matrix()
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
                rawBitmap, 0, 0, width.toInt(),
                height.toInt(), matrix, true
        )
    }

    /**
     * 图标着色
     *
     * @param view       view
     * @param colorResId 颜色
     */
    fun tintImg(view: View, colorResId: Int) {
        if (view is TextView) {
            val drawables = view.compoundDrawables
            val handlers = arrayOfNulls<Drawable>(4)
            for (i in drawables.indices) {
                if (drawables[i] == null) {
                    handlers[i] = null
                    continue
                }
                val d = drawables[i]!!.mutate()
                val temp = DrawableCompat.wrap(d)
                val colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorResId))
                DrawableCompat.setTintList(temp, colorStateList)
                handlers[i] = temp
            }
            view.setCompoundDrawables(handlers[0], handlers[1], handlers[2], handlers[3])
        } else if (view is ImageView) {
            val modeDrawable = view.drawable.mutate()
            val temp = DrawableCompat.wrap(modeDrawable)
            val colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorResId))
            DrawableCompat.setTintList(temp, colorStateList)
            view.setImageDrawable(temp)
        } else {
            return
        }
    }


    /**
     * 图标着色
     *
     * @param res 资源id
     */
    fun tint(res: Int, color: Int = App.context.resources.getColor(R.color.colorPrimary)): Drawable? {
        val modeDrawable: Drawable = App.context.resources.getDrawable(res).mutate()
        val temp = DrawableCompat.wrap(modeDrawable)
        val colorStateList = ColorStateList.valueOf(color)
        DrawableCompat.setTintList(temp, colorStateList)
        return temp
    }

}