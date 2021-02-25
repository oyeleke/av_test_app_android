package com.avtestapp.android.androidbase.av_test.models

import com.avtestapp.android.androidbase.av_test.models.response.Option

data class CurrentSessionDetails(
    val currentQuestion: Int,
    val currentAnswers: HashMap<String, Option>?
)