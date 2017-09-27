package com.mhacks.app.data.kotlin

import com.google.gson.annotations.SerializedName
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose


public class MetaEvents : Parcelable {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("events")
    @Expose
    var events: List<Event>? = null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeList(events)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<MetaEvents> = object : Parcelable.Creator<MetaEvents> {


            override fun createFromParcel(`in`: Parcel): MetaEvents {
                val instance = MetaEvents()
                instance.status = `in`.readValue(Boolean::class.java.classLoader) as Boolean
                `in`.readList(instance.events, Event::class.java.classLoader)
                return instance
            }

            override fun newArray(size: Int): Array<MetaEvents?> {
                return arrayOfNulls(size)
            }

        }
    }

}

class Event : Parcelable {

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String ?= null
    @SerializedName("createdAt")
    @Expose
    var createdAt: String ?= null
    @SerializedName("name")
    @Expose
    var name: String ?= null
    @SerializedName("desc")
    @Expose
    var desc: String ?= null
    @SerializedName("startDate")
    @Expose
    var startDate: String ?= null
    @SerializedName("endDate")
    @Expose
    var endDate: String ?= null
    @SerializedName("location")
    @Expose
    var location: String ?= null
    @SerializedName("category")
    @Expose
    var category: String ?= null
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean? = null
    @SerializedName("createdAt_ts")
    @Expose
    var createdAtTs: Long? = null
    @SerializedName("updatedAt_ts")
    @Expose
    var updatedAtTs: Long? = null
    @SerializedName("startDate_ts")
    @Expose
    var startDateTs: Long? = null
    @SerializedName("endDate_ts")
    @Expose
    var endDateTs: Long? = null
    @SerializedName("id")
    @Expose
    var id: String ?= null

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(updatedAt)
        dest.writeValue(createdAt)
        dest.writeValue(name)
        dest.writeValue(desc)
        dest.writeValue(startDate)
        dest.writeValue(endDate)
        dest.writeValue(location)
        dest.writeValue(category)
        dest.writeValue(deleted)
        dest.writeValue(createdAtTs)
        dest.writeValue(updatedAtTs)
        dest.writeValue(startDateTs)
        dest.writeValue(endDateTs)
        dest.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<Event> = object : Parcelable.Creator<Event> {


            override fun createFromParcel(`in`: Parcel): Event {
                val instance = Event()
                instance.updatedAt = `in`.readValue(String::class.java.classLoader) as String
                instance.createdAt = `in`.readValue(String::class.java.classLoader) as String
                instance.name = `in`.readValue(String::class.java.classLoader) as String
                instance.desc = `in`.readValue(String::class.java.classLoader) as String
                instance.startDate = `in`.readValue(String::class.java.classLoader) as String
                instance.endDate = `in`.readValue(String::class.java.classLoader) as String
                instance.location = `in`.readValue(String::class.java.classLoader) as String
                instance.category = `in`.readValue(String::class.java.classLoader) as String
                instance.deleted = `in`.readValue(Boolean::class.java.classLoader) as Boolean
                instance.createdAtTs = `in`.readValue(Long::class.java.classLoader) as Long
                instance.updatedAtTs = `in`.readValue(Long::class.java.classLoader) as Long
                instance.startDateTs = `in`.readValue(Long::class.java.classLoader) as Long
                instance.endDateTs = `in`.readValue(Long::class.java.classLoader) as Long
                instance.id = `in`.readValue(String::class.java.classLoader) as String
                return instance
            }

            override fun newArray(size: Int): Array<Event?> {
                return arrayOfNulls(size)
            }

        }
    }

}