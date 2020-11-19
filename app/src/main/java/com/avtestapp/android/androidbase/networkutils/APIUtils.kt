/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avtestapp.android.androidbase.networkutils

import com.avtestapp.android.androidbase.av_test.models.response.BaseResponse
import com.avtestapp.android.androidbase.networkutils.GenericErrors.ERROR_UNKNOWN
import com.avtestapp.android.androidbase.networkutils.NetworkConstants.NETWORK_TIMEOUT
import com.avtestapp.android.androidbase.networkutils.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.avtestapp.android.androidbase.networkutils.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

const val GENERIC_ERROR_MESSAGE = "An error occurred, Please try again"
const val GENERIC_ERROR_CODE = "-1"

fun <T : Any> getAPIResult(response: Response<T>): Result<T> {
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            return Result.Success(body)
        }
    } else {

        val errorBody = response.errorBody()
        Timber.e("${(errorBody as ResponseBody).string()}")

        val message = (errorBody as ResponseBody).string()
        if (errorBody != null) {
            return Result.Error(
                getErrorCode(errorBody), getErrorMessage(errorBody)
            )
        }
    }
    return Result.Error("${response.code()}", response.message())
}

fun getErrorMessage(responseBody: ResponseBody): String {
    val gson = Gson()
    val adapter  = gson.getAdapter(BaseResponse::class.java)
    val errorString = responseBody.string()
    var errorResponse = adapter.fromJson(errorString)
    return try {

        val jsonObject = JSONObject(responseBody.string())
        jsonObject.getString("message").replace("_".toRegex(), " ")
    } catch (e: Exception) {
        Timber.e(e)
        GENERIC_ERROR_MESSAGE
    }
}

fun getErrorCode(errorBody: ResponseBody): String {
    return try {
        val errorBodyJsonObject = JSONObject(errorBody.string())
        errorBodyJsonObject.getString("code")
    } catch (e: Exception) {
        Timber.e(e)
        GENERIC_ERROR_CODE
    }
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Timber.e(errorResponse)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    Timber.e(NETWORK_ERROR_UNKNOWN)
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}
