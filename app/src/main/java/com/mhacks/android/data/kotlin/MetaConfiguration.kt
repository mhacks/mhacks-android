package com.mhacks.android.data.kotlin
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MetaConfiguration(
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
    var sqliteID: Int,
    @Expose
    var appName: String,
    @SerializedName("start_date")
    @Expose
    var startDate: String,
    @SerializedName("end_date")
    @Expose
    var endDate: String,
    @SerializedName("createdAt")
    @Expose
    var createdAt: String,
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String,
    @SerializedName("is_application_open")
    @Expose
    var isApplicationOpen: Boolean,
    @SerializedName("is_live_page_enabled")
    @Expose
    var isLivePageEnabled: Boolean,
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean,
    @SerializedName("createdAt_ts")
    @Expose
    var createdAtTs: Long,
    @SerializedName("updatedAt_ts")
    @Expose
    var updatedAtTs: Long,
    @SerializedName("start_date_ts")
    @Expose
    var startDateTs: Long,
    @SerializedName("end_date_ts")
    @Expose
    var endDateTs: Long,
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("should_logout")
    @Expose
    var shouldLogout: Boolean)