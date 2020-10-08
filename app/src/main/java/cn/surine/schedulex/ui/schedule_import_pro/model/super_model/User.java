package cn.surine.schedulex.ui.schedule_import_pro.model.super_model;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.surine.schedulex.data.entity.BaseVm;

@Keep
public class User extends BaseVm implements Serializable {
    public int isRegister;
    public int statusInt;
    public Student student;
}
