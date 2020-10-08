package cn.surine.schedulex.ui.schedule_import_pro.util

import cn.surine.schedulex.base.controller.App
import cn.surine.schedulex.base.utils.Jsons
import cn.surine.schedulex.ui.schedule_import_pro.model.LocalUniversity
import cn.surine.schedulex.ui.schedule_import_pro.model.LocalUniversityInfo
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 * Intro：
 *
 * @author sunliwei
 * @date 10/7/20 17:21
 */
object LocalUniversityManager {
    private val localCodeMap = HashMap<String, LocalUniversity>()
    private val localList = mutableListOf<LocalUniversity>()
    private val localNameMap = HashMap<String, LocalUniversity>()

    init {
        val stream: InputStream = App.context.assets.open("universitylist.json")
        val length = stream.available()
        val buffer = ByteArray(length)
        stream.read(buffer)
        val result = String(buffer, StandardCharsets.UTF_8)
        val data = Jsons.parseJsonWithGsonToList(result, LocalUniversityInfo::class.java)
        for (i in data) {
            localList.addAll(i.schools)
            for (j in i.schools) {
                localCodeMap[j.code] = j
                localNameMap[j.name] = j
            }
        }
    }

    /**
     * 获取某学校 根据code
     * */
    fun getSchoolByCode(code: String): LocalUniversity? {
        return localCodeMap[code]
    }

    /**
     * 获取某学校 根据code
     * */
    fun getSchoolByName(name: String): LocalUniversity? {
        return localNameMap[name]
    }


    /**
     * v1-模糊搜索
     * 自动过滤无code码的学校
     * */
    fun search(keyword: String): List<LocalUniversity?> {
        if (localNameMap.containsKey(keyword)) return listOf(localNameMap[keyword])
        val resultList = mutableListOf<LocalUniversity>()
        localNameMap.keys.forEach {
            if (it.contains(keyword)) {
                val data = localNameMap[it]
                if (data?.code?.isNotEmpty() == true) {
                    resultList.add(data)
                }
            }
        }
        return resultList.toList().sortedBy {
            it.code
        }
    }
}