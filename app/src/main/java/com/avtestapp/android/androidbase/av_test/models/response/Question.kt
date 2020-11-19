package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Question {
    @SerializedName("text")
    @Expose
    var text: String? = null

    @SerializedName("explanation")
    @Expose
    var explanation: String? = null

    @SerializedName("profession")
    @Expose
    var profession: String? = null

    @SerializedName("topic")
    @Expose
    var topic: String? = null

    @SerializedName("options")
    @Expose
    var options: List<Option>? =
        null

    @SerializedName("type")
    @Expose
    var type: String? = null

}