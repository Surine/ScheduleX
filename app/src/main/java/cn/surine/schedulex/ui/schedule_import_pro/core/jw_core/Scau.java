package cn.surine.schedulex.ui.schedule_import_pro.core.jw_core;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse;
import cn.surine.schedulex.ui.schedule_import_pro.model.CourseWrapper;

public class Scau implements IJWParse {

    private final ArrayList<CourseWrapper> resultList = new ArrayList<>();

    /**
     * 入口方法
     *
     * @param html html字符串
     */
    @Override
    @NonNull
    public ArrayList<CourseWrapper> parse(String html) {
        Document document = Jsoup.parse(html);
        System.out.println(document.title() + "\n");

        Elements table = document.getElementsByClass("el-table__body-wrapper");
        Elements tableRow = table.select("tbody").select("tr");

        for (int row = 0; row < 6; row++) {
            /*例 row = 0：1-2节*/
            Elements tableData = tableRow.get(row).select("td");
            for (int col = 1; col < 8; col++) {
                /*例 col = 1：周一*/
                Elements content = tableData.get(col).select("div > div > div");

                /*content: 一个格子中的内容。可能包含多个课程。*/
                int offset = 9; // 每个课程中包含9个div
                int courseNum = content.size() / 9; //格子里包含的课程数
                if (courseNum == 0) {
                    continue;
                }
                CourseWrapper course;
                for (int num = 0; num < courseNum; num++) {
                    course = getCourseByElement(content.get(num * offset), row, col);
                    System.out.println(course.toString());
                    resultList.add(course);
                }
            }
        }
        System.out.println("一共解析了" + resultList.size() + "节课");
        return resultList;
    }

    /**
     * 解析单个课程的内容
     *
     * @param element 单个课程信息节点div
     * @param row     所在行，据此推断节数
     * @param col     所在列，据此推断星期几
     */
    private CourseWrapper getCourseByElement(Element element, int row, int col) {
        Elements info = element.select("div");

        String name = getClassName(info.get(4).text());
        String teacher = info.get(5).text();
        String position = info.get(8).text();
        int sectionStart = getSectionStart(info.get(7).text(), row);
        int sectionContinue = getSectionContinue(info.get(7).text(), row);
        List<Integer> week = getWeeksList(info.get(7).text());

        return new CourseWrapper(name, position, teacher, col, sectionStart, sectionContinue, week);
    }

    /**
     * 获取上课周次
     */
    private ArrayList<Integer> getWeeksList(String str) {
        /*判断单双周*/
        boolean odd = false;
        boolean even = false;
        if (str.contains("单周"))
            odd = true;
        else if (str.contains("双周"))
            even = true;

        /*截取周次信息（因为该串可能包含节次信息）使用括号匹配*/
        String regex = "\\(.*?\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
            str = matcher.group(0);
        str = str.substring(1, str.length() - 2); //去除两端括号和“周”字

        /*使用逗号 分割多个区间*/
        String[] rangeArr = str.split(",");

        ArrayList<Integer> weekList = new ArrayList<Integer>();
        for (String elem : rangeArr) {
            String[] range = elem.split("-");
            if (range.length == 1) {
                weekList.add(Integer.parseInt(range[0].trim())); // 添加trim()以规避可能出现的空格
            } else {
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                for (int i = start; i < end + 1; i++) {
                    /*单双周、或无单双周*/
                    if (even && i % 2 == 0) {
                        weekList.add(i);
                    } else if (odd && i % 2 != 0) {
                        weekList.add(i);
                    } else if (!even && !odd) {
                        weekList.add(i);
                    }
                }
            }
        }
        return weekList;
    }

    /**
     * 获取课程起始节次
     *
     * @param str 用于判断字符串中是否含有指定节次
     * @param row 若无则根据行数，确定起始节次
     */
    private int getSectionStart(String str, int row) {
        if (!str.contains("节")) {
            switch (row) {
                case 0:
                    return 1;
                case 1:
                    return 3;
                case 2:
                    return 6;
                case 3:
                    return 8;
                case 4:
                    return 10;
                default:
                    return 13;
            }
        } else {
            int index = str.indexOf("节");  //“节”字前的表示区间
            str = str.substring(0, index);
            String[] range = str.split("-");
            return Integer.parseInt(range[0].trim());
        }
    }

    /**
     * 获取课程持续节数
     */
    private int getSectionContinue(String str, int row) {
        if (!str.contains("节")) {
            String newStr = "";
            switch (row) {
                case 0:
                    newStr = "01-02节" + str;
                    break;
                case 1:
                    newStr = "03-05节" + str;
                    break;
                case 2:
                    newStr = "06-07节" + str;
                    break;
                case 3:
                    newStr = "08-09节" + str;
                    break;
                case 4:
                    newStr = "10-12节" + str;
                    break;
                default:
                    newStr = "13-15节" + str;
                    break;
            }
            return getSectionContinue(newStr, row);
        } else {
            int index = str.indexOf("节");  //“节”字前的表示区间
            str = str.substring(0, index);
            String[] range = str.split("-");
            int start = Integer.parseInt(range[0].trim());
            int end = Integer.parseInt(range[1].trim());
            return end - start + 1;
        }
    }

    /**
     * 获取课程名，主要是去除星号
     */
    private String getClassName(String str) {
        if (str == null) return "";
        return str.charAt(0) == '*' ? str.substring(2) : str;
    }

    @Override
    public void parseInfo(@NonNull Element element, @NonNull List<CourseWrapper> courseList, int trIndex, int tdIndex, int size) {

    }
}
