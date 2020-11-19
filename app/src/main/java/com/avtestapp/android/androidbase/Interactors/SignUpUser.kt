package com.avtestapp.android.androidbase.Interactors

import com.avtestapp.android.androidbase.av_test.models.requests.RegisterRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.networkutils.Result
import javax.inject.Inject

class SignUpUser @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun signUpUser(signUpRequest: RegisterRequest): Result<LoginSignUpResponse>{
        return authRepository.registerUser(signUpRequest)
    }
}