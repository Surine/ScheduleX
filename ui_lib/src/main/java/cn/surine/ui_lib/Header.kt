import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.surine.ui_lib.R
import cn.surine.ui_lib.SettingItemConfig

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 18:34
 */
@SuppressLint("InflateParams")
class Header(
        val context: Context,
        title: String) {
    val titleView: View by lazy {
        LayoutInflater.from(context)
                .inflate(R.layout.item_group_title, null)
    }


    init {
        titleView.findViewById<TextView>(R.id.title).apply {
            visibility = View.VISIBLE
            text = title
            setTextColor(SettingItemConfig.primaryColor)
        }
    }
}