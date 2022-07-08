package com.example.flowmvvm.data.source.remote.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> {
    @Expose
    @SerializedName("netWorkState")
    var status: Boolean? = null

    @Expose
    @SerializedName("items")
    var data: T? = null

    @Expose
    @SerializedName("maxPage")
    var maxPage: Int = 0
}
