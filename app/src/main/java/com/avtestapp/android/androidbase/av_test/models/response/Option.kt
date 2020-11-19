package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Option {
    @SerializedName("text")
    @Expose
    var text: String? = null

    @SerializedName("isCorrect")
    @Expose
    var isCorrect: Boolean? = null

}