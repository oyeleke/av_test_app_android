package com.avtestapp.android.androidbase.av_test.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OnboardUserRequest(
    @SerializedName("licenseNumber")
    @Expose
    var licenseNumber: String,

    @SerializedName("nationality")
    @Expose
    var nationality: String
)