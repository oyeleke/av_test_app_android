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
package com.avtestapp.android.androidbase.di

import com.avtestapp.android.androidbase.auth.*
import com.avtestapp.android.androidbase.av_test.apis.AuthApiService
import com.avtestapp.android.androidbase.av_test.apis.QuestionsApiService
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.av_test.repository.AuthRepositoryImpl
import com.avtestapp.android.androidbase.av_test.repository.QuestionRepository
import com.avtestapp.android.androidbase.av_test.repository.QuestionRespositoryImpl
import com.avtestapp.android.androidbase.networkutils.NetworkConstants
import com.google.gson.Gson
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [LocalDataModule::class])
class APIServiceModule {

    @Provides
    @Singleton
    fun provideAPIAuthService(
        client: Lazy<OkHttpClient>,
        gson: Gson
    ): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(client.get())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiQuestions(
        client: Lazy<OkHttpClient>,
        gson: Gson
    ): QuestionsApiService{
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(client.get())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(QuestionsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGenericOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BODY

        }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun provideAuthRepository(authApiService: AuthApiService): AuthRepository =
        AuthRepositoryImpl(authApiService)

    @Provides
    @Singleton
    fun provideQuestionRespository(questionsApiService: QuestionsApiService): QuestionRepository =
        QuestionRespositoryImpl(questionsApiService)
}

