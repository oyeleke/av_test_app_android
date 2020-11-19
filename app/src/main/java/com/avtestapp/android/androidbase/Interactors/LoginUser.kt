package com.avtestapp.android.androidbase.Interactors

import com.avtestapp.android.androidbase.av_test.models.requests.LoginRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.networkutils.Result
import javax.inject.Inject

class LoginUser @Inject constructor(
    val authRepository: AuthRepository
){
    suspend fun loginUser(loginRequest: LoginRequest): Result<LoginSignUpResponse>{
        return authRepository.logInUser(loginRequest)
    }
}