package cn.surine.schedulex.base.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/24 17:58
 */
object Imgs{
    @JvmStatic
    fun calculatePicLightValue(path: String):Int{
        var bitmap: Bitmap?=null
        var bitmapSmall: Bitmap? = null
        try {
            val a = System.currentTimeMillis()
            var light = 0.0
            bitmap = BitmapFactory.decodeFile(path)
            bitmapSmall = zoomImage(rawBitmap = bitmap,newWidth = bitmap?.width?.times(0.3)?:0.0,newHeight = bitmap?.height?.times(0.3)?:0.0)
            for (i in 0 until bitmapSmall.width)
                for (j in 0 until bitmapSmall.height) {
                    val argb: Int = bitmapSmall.getPixel(i, j)
                    val r = argb shr 16 and 0xff
                    val g = argb shr 8 and 0xff
                    val b = argb and 0xff
                    light += 0.299 * r + 0.587 * g + 0.114 * b
                }
            val size = bitmapSmall.width*bitmapSmall.height
            return (light/(size)).toInt()
        }catch (e:Exception){
            e.printStackTrace()
            return 0
        }finally {
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
}