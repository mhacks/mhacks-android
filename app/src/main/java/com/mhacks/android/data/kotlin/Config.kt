package com.mhacks.android.data.kotlin
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("configuration")
    @Expose
    var configuration: Configuration
)

@Entity(tableName = "config")
data class Configuration(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @SerializedName("app_name")
    @Expose
    var appName: String,
    @SerializedName("start_date")
    @Expose
    var startDate: String,
    @SerializedName("end_date")
    @Expose
    var endDate: String,
    @SerializedName("is_application_open")
    @Expose
    var isApplicationOpen: Boolean,
    @SerializedName("is_live_page_enabled")
    @Expose
    var isLivePageEnabled: Boolean,
    @SerializedName("should_logout")
    @Expose
    var shouldLogout: Boolean
)