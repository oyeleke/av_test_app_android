package com.avtestapp.android.androidbase.av_test.apis

import com.avtestapp.android.androidbase.av_test.models.response.BaseResponse
import com.avtestapp.android.androidbase.av_test.models.response.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuestionsApiService {


    @GET("questions")
    suspend fun getQuestions(@Header("Authorization") authorization: String, @Query("profession")profession: String): Response<BaseResponse<QuestionResponse>>

}