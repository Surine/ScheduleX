import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import cn.surine.sunny_ui.setting.Group

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/25 18:11
 */
class Setting(val context: Context) {
    private val children = ArrayList<ViewGroup>()

    fun build(): View {
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(10,0,10,0)
        }
        for (viewGroup in children){
            val list = ArrayList<View>()
            for (i in 0 until viewGroup.childCount){
                list.add(viewGroup[i])
            }
            viewGroup.removeAllViews()
            list.forEach {
                root.addView(it)
            }
            list.clear()
        }
        return root
    }

    /**
     * 创建组
     * */
    fun group(title: String = "",block: Group.() -> Unit){
        val group = Group(context,title)
        group.block()
        children.add(group.root)
    }

}
