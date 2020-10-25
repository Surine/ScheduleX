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
const val SELECT_SCHOOL = "select_school"
const val IMPORT_FAIL = "import_fail"
const val UNIVERSITY_IMPORT_SUCCESS = "university_import_success"

fun Fragment.hit(position: String, func: String = CLICK, map: HashMap<String, Any> = HashMap()) {
    val context = this.activity
    MobclickAgent.onEventObject(context, func, map + Pair("position", position))
}


fun Fragment.hit(func: String, data: Pair<String, String>) {
    val context = this.activity
    MobclickAgent.onEventObject(context, func, hashMapOf(data) as Map<String, Any>?)
}





