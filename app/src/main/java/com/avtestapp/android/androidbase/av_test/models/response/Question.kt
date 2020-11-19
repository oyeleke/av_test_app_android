package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Question (
    @SerializedName("_id")
    @Expose
    var id: String,

    @SerializedName("text")
    @Expose
    var text: String,

    @SerializedName("explanation")
    @Expose
    var explanation: String,

    @SerializedName("profession")
    @Expose
    var profession: String,

    @SerializedName("topic")
    @Expose
    var topic: String,

    @SerializedName("options")
    @Expose
    var options: List<Option>,

    @SerializedName("type")
    @Expose
    var type: String,

    @SerializedName("__v")
    @Expose
    var v: Int

)