package cn.surine.schedulex.ui.schedule_import_pro.core.file_core;

import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.base.utils.Jsons;
import cn.surine.schedulex.base.utils.Toasts;
import cn.surine.schedulex.ui.schedule_import_pro.core.IFileParser;
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/13 14:32
 */
public class JsonParser implements IFileParser {
    @Override
    public List<CourseWrapper> parse(String path) {
        try {
            return Jsons.parseJsonWithGsonToList(Files.getFileContent(path), CourseWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
            Toasts.toast("Json解析出错：" + e.getMessage());
            return new ArrayList<>();
        }
    }


}
