package cn.surine.schedulex.base.ktx

import cn.surine.schedulex.base.utils.Jsons
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/29/20 14:16
 */
val gson = Gson()

/**
 * 将Json数据解析成相应的映射对象
 * @param jsonData 数据
 */
inline fun <reified T> parseJson(jsonData: String?): T {
    return gson.fromJson(jsonData, T::class.java)
}


/**
 * 将Json数据解析成相应的映射列表
 *
 * @param json 数据
 */
inline fun <reified T> parseJson2List(json: String?): List<T>? {
    return mutableListOf<T>().apply {
        for (elem in JsonParser.parseString(json).asJsonArray) {
            add(Jsons.gson.fromJson(elem, T::class.java))
        }
    }
}


/**
 * 将实体类转换为String
 * @param data
 */
fun <T> entity2Json(data: List<T>?): String = Jsons.gson.toJson(data)

