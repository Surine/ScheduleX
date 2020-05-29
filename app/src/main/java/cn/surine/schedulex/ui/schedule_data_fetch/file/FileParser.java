package cn.surine.schedulex.ui.schedule_data_fetch.file;

import java.util.List;

import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 * 解析接口
 *
 * @author sunliwei
 * @date 2020/5/13 14:29
 */
public interface FileParser {
    /**
     * 解析文件数据
     */
    List<Course> parse(String path);

}
