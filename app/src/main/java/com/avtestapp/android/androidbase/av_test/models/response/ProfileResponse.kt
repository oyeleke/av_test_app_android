package com.avtestapp.android.androidbase.av_test.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("__v") val version: Int,
    @SerializedName("imageUrl") val imageUrl: String
)