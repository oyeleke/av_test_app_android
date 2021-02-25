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

import com.avtestapp.android.androidbase.av_test.models.response.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

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
        val messageString = """${(errorBody as ResponseBody).string()}"""

        Timber.e(" ======= $messageString")

        val gson = Gson()
        val messageJson = gson.fromJson<ErrorResponse>(messageString, ErrorResponse::class.java)


        val message = messageJson.message
        Timber.e("========= message  ======= $message ========")
        if (message != null) {
            return Result.Error("${response.code()}", message)
        }

    }
    return Result.Error("${response.code()}", response.message())
}



