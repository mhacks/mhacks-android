package org.mhacks.app.game.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.mhacks.app.game.data.model.Quest
import org.mhacks.app.game.data.model.Question
import org.mhacks.app.game.data.model.UserInfo
import java.util.*

/**
 * Various type converters for Room Dao.
 */

class GameStateConverter {

    private fun initMapMoshiAdapter(): JsonAdapter<Map<String, String>> {
        val listMyData = Types.newParameterizedType(
                Map::class.java,
                String::class.java)
        val moshi = Moshi.Builder().build()
        return moshi.adapter(listMyData)
    }

    @TypeConverter
    fun mapFromString(value: String): Map<String, String>? = initMapMoshiAdapter().fromJson(value)

    @TypeConverter
    fun stringFromMap(map: Map<String, String>): String = initMapMoshiAdapter().toJson(map)

    private fun initQuestListMoshiAdapter(): JsonAdapter<List<Quest>> {
        val listMyData = Types.newParameterizedType(
                List::class.java,
                String::class.java)
        val moshi = Moshi.Builder().build()
        return moshi.adapter(listMyData)
    }

    @TypeConverter
    fun questListFromString(value: String): List<Quest>? = initQuestListMoshiAdapter().fromJson(value)

    @TypeConverter
    fun stringFromQuestList(list: List<Quest>): String = initQuestListMoshiAdapter().toJson(list)


    private fun initStringListMoshiAdapter(): JsonAdapter<List<String>> {
        val listMyData = Types.newParameterizedType(
                List::class.java,
                String::class.java)
        val moshi = Moshi.Builder().build()
        return moshi.adapter(listMyData)
    }

    @TypeConverter
    fun stringListFromString(value: String): List<String>? = initStringListMoshiAdapter().fromJson(value)

    @TypeConverter
    fun stringFromStringList(list: List<String>): String = initStringListMoshiAdapter().toJson(list)

}

class UserInfoTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToUserInfo(data: String?): UserInfo {
        if (data == null) {
            return UserInfo("", "")
        }
        val listType = object : TypeToken<UserInfo>() {}.type
        return gson.fromJson<UserInfo>(data, listType)
    }

    @TypeConverter
    fun userinfoToString(userInfo: UserInfo): String {
        return gson.toJson(userInfo)
    }
}

class QuestionTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToQuestion(data: String?): Question {
        if (data == null) {
            return Question("", "", emptyList(), "")
        }
        val listType = object : TypeToken<Question>() {}.type
        return gson.fromJson<Question>(data, listType)
    }

    @TypeConverter
    fun questionToString(question: Question): String {
        return gson.toJson(question)
    }
}

