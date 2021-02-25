package com.avtestapp.android.androidbase.av_test.repository

import com.avtestapp.android.androidbase.av_test.apis.QuestionsApiService
import com.avtestapp.android.androidbase.av_test.models.response.QuestionResponse
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_CODE
import com.avtestapp.android.androidbase.networkutils.GENERIC_ERROR_MESSAGE
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.networkutils.getAPIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class QuestionRespositoryImpl @Inject constructor(
    private val questionsApiService: QuestionsApiService
): QuestionRepository{

    override suspend fun getQuestions(
        bearerAndToken: String,
        profession: String
    ): Result<QuestionResponse> {

        return withContext(Dispatchers.IO){
            try {
                when (val r = getAPIResult(questionsApiService.getQuestions(
                    bearerAndToken,
                    profession
                ))){
                    is Result.Success -> {
                        if (r.result.success){
                            var questionResponse : QuestionResponse
                            if (r.result.data == null){
                                questionResponse = QuestionResponse(
                                    0, 0, 0, 0, ArrayList()
                                )
                            } else {
                                questionResponse = r.result.data
                            }
                            Result.Success(questionResponse)
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

    override suspend fun searchQuestions(
        bearerAndToken: String,
        searchQuestion: String
    ): Result<QuestionResponse> {

        return withContext(Dispatchers.IO){
            try {
                when (val r = getAPIResult(questionsApiService.searchQuestion(
                    bearerAndToken,
                    searchQuestion
                ))){
                    is Result.Success -> {
                        if (r.result.success){
                            var questionResponse: QuestionResponse
                            if(r.result.data == null){
                                questionResponse = QuestionResponse(
                                    currentPage = 0,
                                    limit = 0,
                                    questions = ArrayList(),
                                    total = 0,
                                    totalPages = 0
                                )
                            } else {
                                questionResponse = r.result.data
                            }
                            Result.Success(questionResponse)

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