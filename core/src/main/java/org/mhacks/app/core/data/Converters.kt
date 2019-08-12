package org.mhacks.app.core.data

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Various type converters for Room Dao.
 */
class Converters {

    private fun initMoshiAdapter(): JsonAdapter<List<String>> {
        val listMyData = Types.newParameterizedType(
                List::class.java,
                String::class.java)
        val moshi = Moshi.Builder().build()
        return moshi.adapter(listMyData)
    }

    @TypeConverter
    fun fromString(value: String): List<String>? = initMoshiAdapter().fromJson(value)

    @TypeConverter
    fun fromList(list: List<String>): String = initMoshiAdapter().toJson(list)
}