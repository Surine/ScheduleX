package cn.surine.schedulex.ui.schedule_import_pro.core.shell;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.surine.schedulex.ui.schedule_import_pro.core.IJWParse;
import cn.surine.schedulex.ui.schedule_import_pro.data.CourseWrapper;


/**
 * Intro：
 *  新强智教务-基于华南农业大学
 * @author sunliwei/Grey
 * @date 10/6/20 16:22
 */
public class NewQz implements IJWParse {


    private Document document;
    private ArrayList<CourseWrapper> resultList = new ArrayList<>();

    /**
     * 入口方法
     *
     * @param html html字符串
     */
    public List<CourseWrapper> parse(String html) {
        this.document = Jsoup.parse(html);
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
                    resultList.add(course);
                }
            }
        }
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
        int sectionStart = getSectionStart(row);
        int sectionContinue = getSectionContinue(info.get(7).text());
        List<Integer> week = getWeeksList(info.get(7).text());
        return new CourseWrapper(name, position, teacher, col, sectionStart, sectionContinue, week);
    }

    /**
     * 获取上课周次
     */
    private ArrayList<Integer> getWeeksList(String str) {
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
                    weekList.add(i);
                }
            }
        }
        return weekList;
    }

    /**
     * 获取课程起始节次
     *
     * @param row 根据行数确定起始节次。
     *            例 row = 0 -> 1-2节
     */
    private int getSectionStart(int row) {
        return row * 2 + 1;
    }

    /**
     * 获取课程持续节数
     */
    private int getSectionContinue(String str) {
        if (!str.contains("节")) {
            /*默认为两小节*/
            return 2;
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
    public void parseInfo(@NotNull Element element, @NotNull List<CourseWrapper> courseList, int trIndex, int tdIndex) {
        //un use
    }
}
