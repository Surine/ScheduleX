package cn.surine.schedulex.data.entity;

import java.util.List;

/**
 * Intro：
 * 色卡
 *
 * @author sunliwei
 * @date 2020/6/15 16:58
 */
public class Palette extends BaseVm {
    public long id;
    public String url;
    public String[] colors;
    public String title;

    public Palette(long id, String url, String[] colors, String title) {
        this.id = id;
        this.url = url;
        this.colors = colors;
        this.title = title;
    }
}
