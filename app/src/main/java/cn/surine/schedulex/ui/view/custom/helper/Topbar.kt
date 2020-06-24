package cn.surine.schedulex.ui.view.custom.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.surine.schedulex.R

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/6/26 13:20
 */
class Topbar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    private var titleStr = ""
    private var vTitle: TextView
    private val vFunctionIcon: ImageView

    init {
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.TopBar)
        titleStr = ta.getString(R.styleable.TopBar_title)?:""
        ta.recycle()
        View.inflate(context, R.layout.view_header, this)
        vTitle = findViewById(R.id.title)
        vTitle.text = titleStr
        vFunctionIcon = findViewById(R.id.function)
    }


    fun getFunctionView(): ImageView {
        return vFunctionIcon
    }

    fun setFunctionIcon(src: Int) {
        vFunctionIcon.visibility = View.VISIBLE
        vFunctionIcon.setImageResource(src)
    }

    /**
     * 设置标题
     */
    fun setTitleStr(titleStr: String) {
        this.titleStr = titleStr
        vTitle.text = titleStr
    }
}