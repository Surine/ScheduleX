package cn.surine.schedulex.ui.schedule_import_pro.model.super_model;

import androidx.annotation.Keep;

import cn.surine.schedulex.data.entity.BaseVm;


//这边需要单独keep一下，baseVm失效
@Keep
public class SuperBaseModel<T> extends BaseVm {
    public T data;
    public int status;
}
