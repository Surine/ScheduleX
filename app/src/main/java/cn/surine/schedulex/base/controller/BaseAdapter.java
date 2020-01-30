package cn.surine.schedulex.base.controller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.surine.schedulex.base.utils.Bindings;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-15 20:03
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private List<T> mDatas;
    private int layoutId;
    private int bindName;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BaseAdapter(List<T> mDatas, int layoutId, int bindName) {
        this.mDatas = mDatas;
        this.layoutId = layoutId;
        this.bindName = bindName;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding;
        //设置绑定
        ViewHolder viewHolder = new ViewHolder((viewDataBinding = Bindings.bind(parent, layoutId)).getRoot());
        viewHolder.setBinding(viewDataBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置绑定数据
        holder.getBinding().setVariable(bindName, mDatas.get(position));
        //更新绑定
        holder.getBinding().executePendingBindings();
        //item点击
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}

/**
 * ViewHolder
 */
class ViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    ViewDataBinding getBinding() {
        return binding;
    }

    void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    ViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
