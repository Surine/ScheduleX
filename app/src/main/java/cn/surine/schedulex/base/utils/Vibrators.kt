package cn.surine.schedulex.base.utils

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Vibrator

//震动器
object Vibrators {
    fun vib(context:Context,time:Long){
        val vibrator: Vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time)
    }
}
