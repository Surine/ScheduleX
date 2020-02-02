package cn.surine.schedulex.base.utils;

/**
 * Intro：
 *
 * @author sunliwei
 * @date 2020-01-17 15:39
 */
public class Strs {

    /**
     * 字符串判空
     * @param str 目标字符串
     * */
    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }


    /**
     * 字符串判非空
     * @param str 目标字符串
     * */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }


    /**
     * 字符串比较
     * @param str1 字符串1
     * @param str2 字符串2
     * */
    public static boolean equals(String str1,String str2){
       if(str1 != null){
           return str1.equals(str2);
       }
       return false;
    }


    public static boolean equalsIgnoreCase(String str1,String str2){
        if(str1 != null){
            return str1.equalsIgnoreCase(str2);
        }
        return false;
    }

}
