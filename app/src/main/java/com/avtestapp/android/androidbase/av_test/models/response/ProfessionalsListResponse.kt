package com.avtestapp.android.androidbase.av_test.models.response

import com.google.gson.annotations.SerializedName

data class ProfessionalsListResponse(
    @SerializedName("data")val data: List<ProfessionItemsResponse>? = null
)
