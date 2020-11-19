package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String
)