package org.mhacks.app.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import org.mhacks.app.core.data.Converters
import org.mhacks.app.game.data.QuestionTypeConverter

@TypeConverters(QuestionTypeConverter::class)
data class QuestionResponse(
        @Json(name = "status") var status: Boolean,
        @Json(name = "questions") var questions: List<Question>
)

@TypeConverters(Converters::class)
@Entity(tableName = "questions")
data class Question(
        @PrimaryKey
        @Json(name = "name") var name: String,
        @Json(name = "text") var text: String,
        @Json(name = "options") var options: List<String>,
        @field:Json(name = "task_format") var taskFormat: String
)
