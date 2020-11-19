package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Option (
    @SerializedName("isCorrect")
    @Expose
    var isCorrect: Boolean,

    @SerializedName("_id")
    @Expose
    var id: String,

    @SerializedName("text")
    @Expose
    var text: String

)