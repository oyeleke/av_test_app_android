package com.avtestapp.android.androidbase.av_test.models.requests

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("old") var oldPassword: String,
    @SerializedName("new") var newPassword: String
)