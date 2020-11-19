package com.avtestapp.android.androidbase.av_test.apis

import com.avtestapp.android.androidbase.av_test.models.requests.LoginRequest
import com.avtestapp.android.androidbase.av_test.models.requests.OnboardUserRequest
import com.avtestapp.android.androidbase.av_test.models.requests.RegisterRequest
import com.avtestapp.android.androidbase.av_test.models.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    @POST("login")
    suspend fun logUserIn(@Body request: LoginRequest): Response<BaseResponse<LoginSignUpResponse>>

    @POST("register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<BaseResponse<LoginSignUpResponse>>

    @FormUrlEncoded
    @POST("user/verify")
    suspend fun verifyUserAfterRegistration(@Header("Authorization")authorization: String, @Field("verificationCode")verificationCode: String): Response<BaseResponse<PasswordResetResponse>>

    @FormUrlEncoded
    @POST("user/password/reset")
    suspend fun initiatePasswordReset(@Field("email")email: String): Response<BaseResponse<PasswordResetResponse>>

    @FormUrlEncoded
    @POST("user/password/code")
    suspend fun verifyPasswordResetCode(@Field("code")code: String, @Field("email")email: String): Response<BaseResponse<PasswordResetResponse>>

    @POST("user/password/resend-code")
    suspend fun resendPasswordResetCode(): Response<BaseResponse<PasswordResetResponse>>

    @FormUrlEncoded
    @POST("user/password/new")
    suspend fun createNewPassword(@Field("_id")id: String, @Field("password")password: String): Response<BaseResponse<PasswordResetResponse>>


    @Multipart
    @POST("user/image")
    suspend fun uploadUserImage(@Header("Authorization")authorization: String, @Part  image: MultipartBody.Part ): Response<BaseResponse<PasswordResetResponse>>

    @GET("professions")
    suspend fun getProfessionalsList(@Header("Authorization")authorization: String): Response<BaseResponse< List<ProfessionItemsResponse>>>

    @POST("user/onboard")
    suspend fun onboardUser(@Header("Authorization") authorization: String, @Body onboardUserRequest: OnboardUserRequest) :Response<BaseResponse<Any>>


    @GET("questions")
    suspend fun getQuestions(@Header("Authorization") authorization: String, @Query("profession")profession: String): Response<BaseResponse<QuestionResponse>>
}