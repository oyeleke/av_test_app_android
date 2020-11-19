package com.avtestapp.android.androidbase.av_test.core.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.av_test.repository.QuestionRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import com.google.gson.Gson
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider,
    private val questionRepository: QuestionRepository
) : BaseViewModel() {

    private val _questionTypePickedWithHash = MutableLiveData<Event<QuestionTypeWithHash>>()
    private val _questionType = MutableLiveData<Event<QuestionType>>()
    private val _currentQuestion = MutableLiveData<Event<Question>>()
    private val _currentQuestionNumber = MutableLiveData<Int>()
    private val _totalQuestionNumber = MutableLiveData<Event<Int>>()
    private val questionList = ArrayList<Question>()
    private val _questionListLiveData = MutableLiveData<Event<List<Question>>>()

    val currentQuestion: LiveData<Event<Question>>
    get() = _currentQuestion

    val questionListLiveData : LiveData<Event<List<Question>>>
    get() = _questionListLiveData

    val currentQuestionNumber: LiveData<Int>
    get() = _currentQuestionNumber

    val totalQuestionNumber: LiveData<Event<Int>>
    get() = _totalQuestionNumber


    val questionTypePickedWithHash : LiveData<Event<QuestionTypeWithHash>>
    get() = _questionTypePickedWithHash

    val questionType : LiveData<Event<QuestionType>>
    get() = _questionType



    fun updateQuestionType(questionTypeWithHash: QuestionTypeWithHash){
        _questionTypePickedWithHash.value = Event(questionTypeWithHash)
    }
    fun updateQuestionType(questionTypeWithHash: QuestionType){
        _questionType.value = Event(questionTypeWithHash)
    }


    fun updateCurrentQuestion(index: Int){
        _currentQuestion.value = Event(questionList[index-1])
        _currentQuestionNumber.value = index
    }

    fun getQuestions(professionKey: String){

        Timber.e("getQuestions")
        if(viewModelScope != null){
            Timber.e("viewModelScope Not null")
        } else {
            Timber.e("viewModelScope null")
        }

        viewModelScope.launch {
            _loadingStatus.value =
                LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
            val token = prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).tokenData.token
            val bearer = "Bearer ".plus(token)
            Timber.e("viewModelScope")
            when (val r = questionRepository.getQuestions(bearer, professionKey)) {
                is Result.Success -> {
                    Timber.e("it works ${r.result.questions}")
                    questionList.addAll(r.result.questions)
                    _questionListLiveData.value = Event(r.result.questions)
                    _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.addAll(arrayListOf(currentQuestionNumber, questionListLiveData,currentQuestion, totalQuestionNumber, questionTypePickedWithHash) )
    }

    fun updateTotalQuestionsNumber(totalNumber: Int){
        _totalQuestionNumber.value = Event(totalNumber)
    }

    override fun onCleared() {
        Timber.e("view model has been cleared")
        super.onCleared()
    }
}