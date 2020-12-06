package com.avtestapp.android.androidbase.av_test.repository

import com.avtestapp.android.androidbase.av_test.models.requests.LoginRequest
import com.avtestapp.android.androidbase.av_test.models.requests.OnboardUserRequest
import com.avtestapp.android.androidbase.av_test.models.requests.RegisterRequest
import com.avtestapp.android.androidbase.av_test.models.response.*
import com.avtestapp.android.androidbase.networkutils.Result

interface AuthRepository {
    suspend fun logInUser(loginRequest: LoginRequest): Result<LoginSignUpResponse>

    suspend fun registerUser(registerRequest: RegisterRequest): Result<LoginSignUpResponse>

    suspend fun verifyUserAfterReg(bearerAndToken: String, verificationCode: String): Result<PasswordResetResponse>

    suspend fun getUser(bearerAndToken: String): Result<ProfileResponse>

    suspend fun initiatePasswordReset(email: String): Result<PasswordResetResponse>

    suspend fun verifyPasswordResetCode(resetCode: String, email: String): Result<PasswordResetResponse>

    suspend fun resendPasswordResetCode(): Result<PasswordResetResponse>

    suspend fun createNewPassword(id: String, newPassword: String): Result<PasswordResetResponse>

    suspend fun uploadUserImage(bearerAndToken: String, pathToImage: String): Result<PasswordResetResponse>

    suspend fun getProfessionalsList(bearerAndToken: String): Result< List<ProfessionItemsResponse>>

    suspend fun onboardUser(bearerAndToken: String, onboardUserRequest: OnboardUserRequest): Result<Any>

    suspend fun getQuestions(bearerAndToken: String, profession: String): Result<QuestionResponse>

}