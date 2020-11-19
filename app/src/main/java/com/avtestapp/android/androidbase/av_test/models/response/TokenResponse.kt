package com.avtestapp.android.androidbase.av_test.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String
)