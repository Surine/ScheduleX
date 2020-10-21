package cn.surine.schedulex.app_base

import androidx.fragment.app.Fragment
import com.umeng.analytics.MobclickAgent

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/21/20 10:58
 */

const val CLICK = "click"
const val DATA = "data"

fun Fragment.hit(position: String, func: String = CLICK, map: HashMap<String, Any> = HashMap()) {
    val context = this.activity
    MobclickAgent.onEventObject(context, func, map + Pair("position", position))
}
