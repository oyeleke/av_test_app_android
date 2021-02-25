package com.avtestapp.android.androidbase.av_test.models.requests

import com.google.gson.annotations.SerializedName

data class PostUsersScoreRequest(
    @SerializedName("correctScore")val correctScore: Int,
    @SerializedName("totalScore")val totalScore: Int
)