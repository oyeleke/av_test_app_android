package com.avtestapp.android.androidbase.av_test.repository

import com.avtestapp.android.androidbase.av_test.apis.AuthApiService
import com.avtestapp.android.androidbase.av_test.models.requests.LoginRequest
import com.avtestapp.android.androidbase.av_test.models.requests.OnboardUserRequest
import com.avtestapp.android.androidbase.av_test.models.requests.RegisterRequest
import com.avtestapp.android.androidbase.av_test.models.response.*
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_CODE
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_MESSAGE
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.networkutils.getAPIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun logInUser(loginRequest: LoginRequest): Result<LoginSignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(authApiService.logUserIn(loginRequest))) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }

            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }

        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): Result<LoginSignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r =
                    getAPIResult(authApiService.registerUser(registerRequest))) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun verifyUserAfterReg(
        bearerAndToken: String,
        verificationCode: String
    ): Result<PasswordResetResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.verifyUserAfterRegistration(
                        bearerAndToken,
                        verificationCode
                    )
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getUser(bearerAndToken: String): Result<ProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.getUser(
                        bearerAndToken)
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun initiatePasswordReset(email: String): Result<PasswordResetResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.initiatePasswordReset(email)
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun verifyPasswordResetCode(
        resetCode: String,
        email: String
    ): Result<PasswordResetResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.verifyPasswordResetCode(
                        resetCode,
                        email
                    )
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun resendPasswordResetCode(): Result<PasswordResetResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.resendPasswordResetCode()
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun createNewPassword(
        id: String,
        newPassword: String
    ): Result<PasswordResetResponse> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.createNewPassword(
                        id = id,
                        password = newPassword
                    )
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun uploadUserImage(
        bearerAndToken: String,
        pathToImage: String
    ): Result<PasswordResetResponse> {
        val userImageFile = File(pathToImage)
        val userImage: RequestBody = userImageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val userImageMultipartBody = MultipartBody.Part.createFormData(
            "image",
            userImageFile.name,
            userImage
        )
        return withContext(Dispatchers.IO) {
            try {

                when (val r = getAPIResult(
                    authApiService.uploadUserImage(
                        bearerAndToken, userImageMultipartBody
                    )
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getProfessionalsList(bearerAndToken: String): Result<List<ProfessionItemsResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                when (val r = getAPIResult(
                    authApiService.getProfessionalsList(bearerAndToken)
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)

            }
        }
    }

    override suspend fun onboardUser(
        bearerAndToken: String,
        onboardUserRequest: OnboardUserRequest
    ): Result<Any> {
        return withContext(Dispatchers.IO) {

            try {
                when (val r = getAPIResult(
                    authApiService.onboardUser(bearerAndToken, onboardUserRequest)
                )) {
                    is Result.Success -> {
                        if (r.result.success) {
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception) {

                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }

    override suspend fun getQuestions(
        bearerAndToken: String,
        profession: String
    ): Result<QuestionResponse> {

        return withContext(Dispatchers.IO){
            try {
                when (val r = getAPIResult(authApiService.getQuestions(
                    bearerAndToken,
                    profession
                ))){
                    is Result.Success -> {
                        if (r.result.success){
                            Result.Success(r.result.data)
                        } else {
                            Result.Error(GENERIC_ERROR_CODE, r.result.message)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(r.errorCode, r.errorMessage)
                    }
                }
            } catch (e: Exception){
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }



}