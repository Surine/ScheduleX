package cn.surine.schedulex.ui.view.custom.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.surine.schedulex.R;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-03-04 16:03
 */
public class TopBar extends LinearLayout {

    String titleStr = "";
    private TextView vTitle;

    public TopBar(Context context) {
        this(context,null);
    }


    public TopBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TopBar);
        titleStr = ta.getString(R.styleable.TopBar_title);
        ta.recycle();
        init(context);
    }


    private void init(Context context) {
        inflate(context, R.layout.view_header, this);
        vTitle = findViewById(R.id.title);
        vTitle.setText(titleStr);
    }


    /**
     * 设置标题
     */
    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        if (vTitle != null) {
            vTitle.setText(titleStr);
        }
    }
}
