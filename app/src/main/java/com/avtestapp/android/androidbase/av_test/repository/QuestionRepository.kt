package com.avtestapp.android.androidbase.av_test.repository

import com.avtestapp.android.androidbase.av_test.models.response.QuestionResponse
import com.avtestapp.android.androidbase.networkutils.Result

interface QuestionRepository {

    suspend fun getQuestions(bearerAndToken: String, profession: String): Result<QuestionResponse>
}