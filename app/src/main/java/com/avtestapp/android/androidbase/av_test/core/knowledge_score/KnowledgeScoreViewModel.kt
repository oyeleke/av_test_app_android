package com.avtestapp.android.androidbase.av_test.core.knowledge_score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.requests.PostUsersScoreRequest
import com.avtestapp.android.androidbase.av_test.models.response.GetScoresResponse
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.av_test.repository.QuestionRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class KnowledgeScoreViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider,
    private val authRepository: AuthRepository
) : BaseViewModel() {
    private val _onScoreGottenObserver = MutableLiveData<Event<Float>>()

    val onScoreGottenObserver : LiveData<Event<Float>>
    get() = _onScoreGottenObserver


    fun getScores(){
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
            val token = prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).tokenData.token
            val bearer = "Bearer ".plus(token)
            when (val result = authRepository.getScore(
                bearerAndToken = bearer
            )) {
                is Result.Success -> {
                    _onScoreGottenObserver.value = Event(result.result)
                    _loadingStatus.value = LoadingStatus.Success
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }

        }
    }

    override fun addAllLiveDataToObservablesList() {

    }
 
}