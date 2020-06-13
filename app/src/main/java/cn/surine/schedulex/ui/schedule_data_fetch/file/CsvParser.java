package cn.surine.schedulex.ui.schedule_data_fetch.file;

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
import cn.surine.schedulex.data.entity.Course;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020/5/13 14:43
 */
public class CsvParser implements FileParser {
    @Override
    public List<Course> parse(String path) {
        List<Course> courses = new ArrayList<>();
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Files.getFileContent(path).getBytes())));
        try {
            Iterable<CSVRecord> records = format.parse(reader);
            Course course = new Course();
            for (CSVRecord record : records) {
                course.coureName = record.get("courseName");
                course.teacherName = record.get("teacherName");
                course.classWeek = record.get("classWeek");
                course.classDay = record.get("classDay");
                course.classSessions = record.get("classSessions");
                course.continuingSession = record.get("continuingSession");
                course.teachingBuildingName = record.get("teachingBuildingName");
                course.xf = record.get("xf");
                course.color = record.get("color");
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
}
