package cn.surine.schedulex.ui.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.surine.schedulex.R;
import cn.surine.schedulex.base.utils.Objs;

/**
 * Intro：空视图
 * @author sunliwei
 * @date 2020-01-15 13:16
 */
public class EmptyView extends LinearLayout {

    private ImageView vLogo;
    private TextView vTitle;
    private Button vAction;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_empty, this);
        vLogo = findViewById(R.id.empty_logo);
        vTitle = findViewById(R.id.empty_title);
        vAction = findViewById(R.id.empty_action);
    }


    public EmptyView setResId(final int resId) {
        Objs.notNullCall(vLogo, () -> vLogo.setImageResource(resId));
        return this;
    }

    public EmptyView setTitle(String title) {
        Objs.notNullCall(vTitle, () -> vTitle.setText(title));
        return this;
    }

    public EmptyView setBtnText(String btnText) {
        Objs.notNullCall(vAction, () -> vAction.setText(btnText));
        return this;
    }

    public EmptyView addClickListener(OnClickListener onClickListener) {
        Objs.notNullCall(vAction, () -> vAction.setOnClickListener(onClickListener));
        return this;
    }
}
