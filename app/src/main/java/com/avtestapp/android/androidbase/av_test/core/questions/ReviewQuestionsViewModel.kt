package com.avtestapp.android.androidbase.av_test.core.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.models.response.Option
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import java.util.ArrayList
import javax.inject.Inject

class ReviewQuestionsViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {

    var setCurrentQuestion = false
        private set

    private val _questionTypePickedWithHash = MutableLiveData<Event<QuestionTypeWithHash>>()
    private val _questionType = MutableLiveData<Event<QuestionType>>()
    private val _currentQuestion = MutableLiveData<Event<Question>>()
    private val _currentQuestionNumber = MutableLiveData<Int>()
    private val _totalQuestionNumber = MutableLiveData<Event<Int>>()
    private val questionList = ArrayList<Question>()
    private val _questionListLiveData = MutableLiveData<Event<List<Question>>>()
    private val _practiceQuestionSessionAnswers = MutableLiveData<HashMap<String, Option>>(HashMap())

    val practiceQuestionSessionAnswers: LiveData<HashMap<String, Option>>
        get() = _practiceQuestionSessionAnswers

    val currentQuestion: LiveData<Event<Question>>
        get() = _currentQuestion

    val questionListLiveData: LiveData<Event<List<Question>>>
        get() = _questionListLiveData

    val currentQuestionNumber: LiveData<Int>
        get() = _currentQuestionNumber

    val totalQuestionNumber: LiveData<Event<Int>>
        get() = _totalQuestionNumber



    fun updateCurrentQuestion(index: Int) {
        _currentQuestion.value = Event(questionList[index - 1])
        _currentQuestionNumber.value = index
    }


    fun getLastQuestionsAnswered(){
        val questions = sharedPrefs.getCurrentReviewQuestions()
        if (questions != null){
            _questionListLiveData.value = Event(questions.questionList)
            questionList.addAll(questions.questionList)
            _practiceQuestionSessionAnswers.value = sharedPrefs.getCurrentSessionDetails()!!.currentAnswers

        }
    }

    fun updateTotalQuestionsNumber(totalNumber: Int) {
        _totalQuestionNumber.value = Event(totalNumber)
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.addAll(
            arrayListOf(
                currentQuestionNumber,
                questionListLiveData,
                currentQuestion,
                totalQuestionNumber
            )
        )
    }


}