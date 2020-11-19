package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.SerializedName

data class ProfessionItemsResponse(
    @SerializedName("_id")val id: String,
    @SerializedName("name")val name: String,
    @SerializedName("__v")val version: Int
){
    override fun toString(): String {
        return name
    }
}