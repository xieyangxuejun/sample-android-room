package cn.foretree.db

import android.arch.persistence.room.TypeConverter

/**
 * 集合类型转换
 * Created by silen on 04/09/2018
 */
object NomalTypeConverter {
    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<String>? {
        return data?.split(", ")
    }

    @TypeConverter
    @JvmStatic
    fun listToString(list: List<String>?): String? {
        return list?.joinToString()
    }

}
