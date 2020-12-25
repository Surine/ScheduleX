package cn.surine.schedulex.base.utils

import android.util.JsonReader
import android.webkit.JsResult
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/25/20 09:47
 */
object Jsons {

    val gson = Gson()

    /**
     * 将Json数据解析成相应的映射对象
     * @param jsonData 数据
     */
    inline fun <reified T> parseJsonWithGson(jsonData: String?): T {
        return gson.fromJson(jsonData, T::class.java)
    }


    /**
     * 将Json数据解析成相应的映射列表
     *
     * @param json 数据
     */
    inline fun <reified T> parseJsonWithGsonToList(json: String?): List<T>? {
        return mutableListOf<T>().apply {
            for (elem in JsonParser.parseString(json).asJsonArray) {
                add(gson.fromJson(elem, T::class.java))
            }
        }
    }


    /**
     * 将实体类转换为String
     * @param data
     */
    fun <T> entityToJson(data: List<T>?): String = gson.toJson(data)

}