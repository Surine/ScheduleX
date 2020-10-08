package cn.surine.schedulex.ui.schedule_import_pro.core;

import java.util.List;

import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper;

/**
 * Intro：
 * 解析接口
 *
 * @author sunliwei
 * @date 2020/5/13 14:29
 */
public interface IFileParser extends ICommonParse{
    /**
     * 解析文件数据
     */
    List<CourseWrapper> parse(String path);

}
