package cn.surine.schedulex.ui.schedule_data_fetch.file;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.Jsons;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/13 14:32
 */
public class JsonParser implements FileParser {
    @Override
    public List<Course> parse(String path) {
        try {
            return Jsons.parseJsonWithGsonToList(Files.getFileContent(path), Course.class);
        } catch (Exception e) {
            e.printStackTrace();
            Toasts.toast("Json解析出错：" + e.getMessage());
            return new ArrayList<>();
        }
    }


}
