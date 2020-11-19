package com.avtestapp.android.androidbase.av_test.models.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardUserRequest {
    @SerializedName("licenseNumber")
    @Expose
    var licenseNumber: String? = null

    @SerializedName("nationality")
    @Expose
    var nationality: String? = null

}