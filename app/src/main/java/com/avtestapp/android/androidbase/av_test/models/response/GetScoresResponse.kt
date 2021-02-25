package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.SerializedName

data class GetScoresResponse(
    @SerializedName("score")val score: Float? = 0f
)