package com.example.flowmvvm.data.source.local.dao

import androidx.room.TypeConverter
import com.example.flowmvvm.utils.extension.notNull
import com.google.gson.Gson
import java.util.*

object JsonConverter {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun toJson(objects: Any?, gson: Gson = Gson()): String? {
        objects.notNull {
            return gson.toJson(it)
        }
        return null
    }
    
    @TypeConverter
    fun <T> toObject(json: String?, type: Class<T>, gson: Gson = Gson()): T? {
        json.notNull {
            return gson.fromJson(it, type)
        }
        return null
    }
}