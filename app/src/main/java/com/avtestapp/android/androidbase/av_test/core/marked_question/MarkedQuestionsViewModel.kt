
package com.avtestapp.android.androidbase.av_test.core.marked_question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

class MarkedQuestionsViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
)  : BaseViewModel() {

    private val _currentQuestion = MutableLiveData<Event<Question>>()
    private val _currentQuestionNumber = MutableLiveData<Int>()
    private val _totalQuestionNumber = MutableLiveData<Event<Int>>()
    private val questionList = ArrayList<Question>()
    private val _questionListLiveData = MutableLiveData<Event<List<Question>>>()

    private val _currentQuestionPractice = MutableLiveData<Event<Question>>()
    private val _currentQuestionNumberPractice = MutableLiveData<Int>()
    private val _totalQuestionNumberPractice = MutableLiveData<Event<Int>>()
    private val questionListPractice = ArrayList<Question>()
    private val _questionListLiveDataPractice = MutableLiveData<Event<List<Question>>>()


    var isPracticeQuestion = true

    val currentQuestion: LiveData<Event<Question>>
        get() = _currentQuestion

    val questionListLiveData: LiveData<Event<List<Question>>>
        get() = _questionListLiveData

    val currentQuestionNumber: LiveData<Int>
        get() = _currentQuestionNumber

    val totalQuestionNumber: LiveData<Event<Int>>
        get() = _totalQuestionNumber

    val currentQuestionPractice: LiveData<Event<Question>>
        get() = _currentQuestionPractice

    val questionListLiveDataPractice: LiveData<Event<List<Question>>>
        get() = _questionListLiveDataPractice

    val currentQuestionNumberPractice: LiveData<Int>
        get() = _currentQuestionNumberPractice

    val totalQuestionNumberPractice: LiveData<Event<Int>>
        get() = _totalQuestionNumberPractice



    fun updateCurrentQuestion(index: Int) {
        _currentQuestion.value = Event(questionList[index - 1])
        _currentQuestionNumber.value = index
    }

    fun updateCurrentQuestionPractice(index: Int) {
        _currentQuestionPractice.value = Event(questionListPractice[index - 1])
        _currentQuestionNumberPractice.value = index
    }

    fun updateTotalQuestionsNumberPractice(totalNumber: Int) {
        _totalQuestionNumberPractice.value = Event(totalNumber)
    }

    fun updateTotalQuestionsNumber(totalNumber: Int) {
        _totalQuestionNumber.value = Event(totalNumber)
    }

    fun getQuestionsPractice(isPracticeQuestion: Boolean){
        this.isPracticeQuestion = isPracticeQuestion
        val bookMarkedQuestions = sharedPrefs.getSavedBookMarked()
        var questionListLocal: List<Question> = bookMarkedQuestions.questionList.filter { it.isPracticeQuestion }
        Timber.e("============= ${questionListLocal.size}  ${isPracticeQuestion}==============")
        _questionListLiveDataPractice.value = Event(questionListLocal)
        questionListPractice.addAll(questionListLocal)
    }

    fun getQuestionsStudy(isPracticeQuestion: Boolean){
        this.isPracticeQuestion = isPracticeQuestion
        val bookMarkedQuestions = sharedPrefs.getSavedBookMarked()
        var questionListLocal: List<Question> = ArrayList()
        Timber.e("============= ${questionListLocal.size}  ${isPracticeQuestion}==============")
        _questionListLiveData.value = Event(questionListLocal)
        questionList.addAll(questionListLocal)
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.addAll(
            arrayListOf(
                currentQuestionNumber,
                questionListLiveData,
                currentQuestion,
                totalQuestionNumber,
                currentQuestionNumberPractice,
                questionListLiveDataPractice,
                currentQuestionPractice,
                totalQuestionNumberPractice
            )
        )
    }

}