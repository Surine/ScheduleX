import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.surine.ui_lib.R

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 18:38
 */
class Item(
        val context: Context,
        title: String,
        subTitle: String = "",
        onClick: Item.(view: View) -> Unit
) {
    val view by lazy {
        LayoutInflater.from(context)
                .inflate(R.layout.item_normal_setting, null)
    }

    val vTitle: TextView
    val vSubTitle: TextView

    init {
        vTitle = view.findViewById(R.id.title)
        vSubTitle = view.findViewById(R.id.subtitle)
        vTitle.text = title
        if (subTitle.isNotEmpty()) {
            vSubTitle.visibility = View.VISIBLE
            vSubTitle.text = subTitle
        } else {
            vSubTitle.visibility = View.GONE
        }
        view.setOnClickListener {
            onClick(it)
        }
    }
}