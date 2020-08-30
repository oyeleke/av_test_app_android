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
package com.avtestapp.android.androidbase.av_test

import androidx.lifecycle.Transformations
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_CODE
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_MESSAGE
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.networkutils.getAPIResult
import com.avtestapp.android.androidbase.av_test.apis.ExampleApiService
import com.avtestapp.android.androidbase.av_test.models.BreedDatabase
import com.avtestapp.android.androidbase.av_test.models.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    private val exampleApiService: ExampleApiService,
    private val breedDatabase: BreedDatabase
) {

    val breeds = Transformations.map(breedDatabase.breedDao.getAllBreed()) {
        it.asDomainModel()
    }

    suspend fun refreshBreeds(limit: Int) {
        withContext(Dispatchers.IO) {
            try {
                when (val result = getAPIResult(exampleApiService.getCatBreeds(limit))) {
                    is Result.Success -> {
                        breedDatabase.breedDao.dropTable()
                        breedDatabase.breedDao.insertAllBreed(*result.data.asDataBaseModel())
                    }
                    is Result.Error -> {
                        Result.Error(GENERIC_ERROR_CODE, result.errorMessage)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(GENERIC_ERROR_CODE, e.message ?: GENERIC_ERROR_MESSAGE)
            }
        }
    }
}
