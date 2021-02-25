package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Topic (
    @SerializedName("_id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("key")
    @Expose
    var key: String,

    @SerializedName("__v")
    @Expose
    var v: Int

)