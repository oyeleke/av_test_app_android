package com.avtestapp.android.androidbase.av_test.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PasswordResetResponse(
    @SerializedName("verified")val verified: Boolean,
    @SerializedName("_id")val _id: String,
    @SerializedName("email")val email: String,
    @SerializedName("firstName")val firstName: String,
    @SerializedName("lastName")val lastName: String,
    @SerializedName("createdAt")val createdAt: String,
    @SerializedName("__v")val __v: Int,
    @SerializedName("imageUrl")val imageUrl :String
): Parcelable