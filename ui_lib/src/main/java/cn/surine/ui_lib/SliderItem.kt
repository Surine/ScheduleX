import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.surine.ui_lib.R
import com.google.android.material.slider.Slider

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 22:37
 */
class SliderItem(
        val context: Context,
        title: String,
        subTitle: String = "",
        valueFrom: Float = 0F,
        valueTo: Float = 100F,
        step: Float = 0F,
        initValue: Float = 0F,
        tag: String = "",
        onDataChanged: SliderItem.(view: Slider, value: Float) -> Unit
) {
    val view: View by lazy {
        LayoutInflater.from(context)
                .inflate(R.layout.item_slider_setting, null)
    }
    private val sp = context.getSharedPreferences("data", Context.MODE_PRIVATE)

    private val vTitle: TextView
    private val vSubTitle: TextView
    private val vSlider: Slider
    private val vSliderNum: TextView

    init {
        vTitle = view.findViewById(R.id.title)
        vSubTitle = view.findViewById(R.id.subtitle)
        vSlider = view.findViewById(R.id.slider)
        vSliderNum = view.findViewById(R.id.slider_num)
        vTitle.text = title
        vSlider.valueFrom = valueFrom
        vSlider.valueTo = valueTo
        vSlider.stepSize = step
        vSlider.value = sp.getFloat(tag, initValue)
        if (subTitle.isNotEmpty()) {
            vSubTitle.visibility = View.VISIBLE
            vSubTitle.text = subTitle
        } else {
            vSubTitle.visibility = View.GONE
        }
        vSlider.addOnChangeListener { _, value, _ ->
            if (tag.isNotEmpty()) {
                sp.edit().putFloat(tag, value).apply()
            }
            onDataChanged(vSlider, value)
        }
    }
}