package cn.surine.schedulex.base.ktx

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

/**
 * Intro：
 *
 * @author sunliwei
 * @date 11/26/20 14:29
 */
fun Context.isValid():Boolean{
    if(this is Activity){
        if(this.isDestroyed || this.isFinishing){
            return false
        }
        return true
    }else if(this is Fragment){
        if(this.isDetached){
            return false
        }
    }
    return true
}