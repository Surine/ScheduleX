package cn.surine.schedulex.base.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/5/14.
 */

public class Jsons {


    /**
     * 将Json数据解析成相应的映射对象
     *
     * @param jsonData 数据
     * @param type     类型
     */
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    /**
     * 将Json数据解析成相应的映射列表
     *
     * @param json 数据
     * @param cls  类型
     */
    public static <T> List<T> parseJsonWithGsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }


    /**
     * 将实体类转换为String
     * @param data
     * */
    public static <T> String entityToJson(List<T> data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}