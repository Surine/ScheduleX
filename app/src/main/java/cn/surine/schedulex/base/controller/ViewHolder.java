package cn.surine.schedulex.base.controller;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder
 */
public class ViewHolder extends RecyclerView.ViewHolder {
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
