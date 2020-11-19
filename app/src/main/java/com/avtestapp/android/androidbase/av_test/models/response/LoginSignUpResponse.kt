package com.avtestapp.android.androidbase.av_test.models.response

import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
import com.avtestapp.android.androidbase.av_test.models.response.TokenResponse
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("tokenData")val tokenData: TokenResponse,
    @SerializedName("profile")val profile: ProfileResponse
)