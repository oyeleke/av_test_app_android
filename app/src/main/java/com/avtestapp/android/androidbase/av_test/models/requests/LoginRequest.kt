package com.avtestapp.android.androidbase.av_test.models.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)