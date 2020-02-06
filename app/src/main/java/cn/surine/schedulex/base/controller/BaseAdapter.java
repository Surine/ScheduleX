package cn.surine.schedulex.base.controller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.surine.schedulex.base.utils.Bindings;

/**
 * Intro：
 * 基于bind的通用RecyclerView适配器
 *
 * @author sunliwei
 * @date 2020-01-15 20:03
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private List<T> mDatas;
    private int layoutId;
    private int bindName;


    private OnItemClickListener onItemClickListener;
    private OnMyItemLongClickListener onItemLongClickListener;
    private HashMap<Integer, OnItemElementClickListener> itemElementClickListenerHashMap = new HashMap<>();


    /**
     * 用于实现item点击事件的接口
     */
    public interface OnItemClickListener {

        /**
         * 实现此方法用于接受点击事件的响应
         *
         * @param position 点击位置
         */
        void onClick(int position);
    }


    public interface OnMyItemLongClickListener {
        /**
         * 实现此方法用于接受点击事件的响应
         *
         * @param position 点击位置
         */
        boolean onClick(int position);
    }


    /**
     * 用于实现item子view点击事件的抽象类
     */
    public abstract static class OnItemElementClickListener {
        public int id;

        public OnItemElementClickListener(int id) {
            this.id = id;
        }

        /**
         * 实现此方法用于接受点击事件的响应
         *
         * @param v        被点击view
         * @param position 点击位置
         */
        public abstract void onClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setOnItemElementClickListener(@NotNull OnItemElementClickListener onItemElementClickListener) {
        itemElementClickListenerHashMap.put(onItemElementClickListener.id, onItemElementClickListener);
    }

    public void setOnItemLongClickListener(OnMyItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BaseAdapter(List<T> mDatas, int layoutId, int bindName) {
        this.mDatas = mDatas;
        this.layoutId = layoutId;
        this.bindName = bindName;
    }

    private boolean banRecycle;

    public void setBanRecycle(boolean banRecycle) {
        this.banRecycle = banRecycle;
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
        if (banRecycle) {
            viewHolder.setIsRecyclable(false);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置绑定数据
        holder.getBinding().setVariable(bindName, mDatas.get(position));
        //更新绑定
        holder.getBinding().executePendingBindings();
        //item点击
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(position));
        }

        //长按事件
        if(onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.onClick(position));
        }

        //item子项点击
        for (Map.Entry<Integer, OnItemElementClickListener> entry : itemElementClickListenerHashMap.entrySet()) {
            try {
                holder.itemView.findViewById(entry.getKey()).setOnClickListener(v -> entry.getValue().onClick(v, position));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
