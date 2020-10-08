package cn.surine.schedulex.ui.schedule_import_pro.core.file_core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
    @Override
    public List<CourseWrapper> parse(String path) {
        List<CourseWrapper> courses = new ArrayList<>();
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Files.getFileContent(path).getBytes())));
        try {
            Iterable<CSVRecord> records = format.parse(reader);
            CourseWrapper course = new CourseWrapper();
            for (CSVRecord record : records) {
                course.setName(record.get("name"));
                course.setTeacher(record.get("teacher"));
                course.setWeek(getWeekList(record.get("week")));
                course.setDay(Integer.parseInt(record.get("day")));
                course.setSectionStart(Integer.parseInt(record.get("sectionStart")));
                course.setSectionContinue(Integer.parseInt(record.get("sectionContinue")));
                course.setPosition(record.get("position"));
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
}
