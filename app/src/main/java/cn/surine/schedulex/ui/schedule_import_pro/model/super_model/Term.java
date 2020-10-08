package cn.surine.schedulex.ui.schedule_import_pro.model.super_model;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/4/9 20:07
 */
@Keep
public class Term implements Serializable {
    public int beginYear;
    public int term;

    public String getDesStr() {
        String suffix = term == 1 ? "秋季学期" : "春季学期";
        return beginYear + "年" + suffix;
    }
}
