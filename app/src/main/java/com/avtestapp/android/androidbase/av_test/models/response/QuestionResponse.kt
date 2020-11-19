package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class QuestionResponse (
    @SerializedName("currentPage")
    @Expose
    var currentPage: Int,

    @SerializedName("totalPages")
    @Expose
    var totalPages: Int,

    @SerializedName("limit")
    @Expose
    var limit: Int,

    @SerializedName("total")
    @Expose
    var total: Int,

    @SerializedName("questions")
    @Expose
    var questions: List<Question>

)