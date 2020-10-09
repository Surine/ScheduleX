package cn.surine.schedulex.ui.schedule_import_pro.core.file_core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.surine.schedulex.base.utils.Files;
import cn.surine.schedulex.ui.schedule_import_pro.core.IFileParser;
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/13 14:43
 */
public class CsvParser implements IFileParser {
    static final String[] FILE_HEADER = {"name", "teacher", "position", "sectionStart", "sectionContinue", "day", "week"};
    private final static String NEW_LINE_SEPARATOR="\n";

    @Override
    public List<CourseWrapper> parse(String path) {
        List<CourseWrapper> courses = new ArrayList<>();
        CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER).withSkipHeaderRecord();
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Files.getFileContent(path).getBytes())));
        try {
            Iterable<CSVRecord> records = format.parse(reader);
            CourseWrapper course = new CourseWrapper();
            for (CSVRecord record : records) {
                course.setName(record.get("name"));
                course.setTeacher(record.get("teacher"));
                course.setPosition(record.get("position"));
                course.setSectionStart(Integer.parseInt(record.get("sectionStart")));
                course.setSectionContinue(Integer.parseInt(record.get("sectionContinue")));
                course.setDay(Integer.parseInt(record.get("day")));
                course.setWeek(getWeekList(record.get("week")));
                courses.add(course);
            }
        } catch (Exception e) {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return courses;
    }

    private List<Integer> getWeekList(String classWeek) {
        String[] weeks = classWeek.split(" ");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < weeks.length; i++) {
            list.add(Integer.valueOf(weeks[i]));
        }
        return list;
    }


    /**
     * 写入csv文件
     * @param data     数据内容
     * @param filePath 创建的csv文件路径
     * @throws IOException
     **/
    public static void writeCsv(List<String[]> data, String filePath) throws IOException {
        //初始化csvformat
        CSVFormat formator = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        //创建FileWriter对象
        FileWriter fileWriter = new FileWriter(filePath);
        //创建CSVPrinter对象
        CSVPrinter printer = new CSVPrinter(fileWriter, formator);
        //写入列头数据
        printer.printRecord(FILE_HEADER);
        if (null != data) {
            //循环写入数据
            for (String[] lineData : data) {
                printer.printRecord(lineData);
            }
        }
    }
}
