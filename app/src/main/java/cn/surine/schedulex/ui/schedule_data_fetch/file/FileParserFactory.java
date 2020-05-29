package cn.surine.schedulex.ui.schedule_data_fetch.file;

import android.os.Bundle;

import cn.surine.schedulex.base.controller.AbstractSingleTon;

/**
 * Intro：
 * 文件解析
 *
 * @author sunliwei
 * @date 2020/5/13 14:32
 */
public class FileParserFactory {

    public static final String JSON = "json";
    public static final String EXCEL_XLS = "xls";
    public static final String EXCEL_XLSX = "xlsx";
    public static final String CSV = "csv";

    public static AbstractSingleTon<FileParserFactory> abt = new AbstractSingleTon<FileParserFactory>() {
        @Override
        protected FileParserFactory newObj(Bundle bundle) {
            return new FileParserFactory();
        }
    };


    public FileParser get(String suffix) {
        switch (suffix) {
            case JSON:
            default:
                return new JsonParser();
            case EXCEL_XLS:
            case EXCEL_XLSX:
                return new ExcelParser();
            case CSV:
                return new CsvParser();
        }
    }
}
