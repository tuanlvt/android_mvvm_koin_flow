package com.example.flowmvvm.data.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

open class BaseModel<T> {
    @Expose
    @SerializedName("id")
    var id: Int = UNKNOWN_ID

    @Expose
    @SerializedName("name")
    var name: String? = ""

    @Expose
    @SerializedName("date")
    var date: Date? = null

    fun clone(): T {
        val stringProject = Gson().toJson(this, this::class.java)
        return Gson().fromJson<T>(stringProject, this::class.java)
    }

    companion object {
        const val UNKNOWN_ID = -1
    }
}