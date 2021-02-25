package com.avtestapp.android.androidbase.av_test.core.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.models.requests.PostUsersScoreRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.Option
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.av_test.repository.QuestionRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.FormField
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateNotEmpty
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider,
    private val questionRepository: QuestionRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    var setCurrentQuestion = false
        private set

    private val _questionTypePickedWithHash = MutableLiveData<Event<QuestionTypeWithHash>>()
    private val _questionType = MutableLiveData<Event<QuestionType>>()
    private val _currentQuestion = MutableLiveData<Event<Question>>()
    private val _currentQuestionNumber = MutableLiveData<Int>()
    private val _totalQuestionNumber = MutableLiveData<Event<Int>>()
    private val questionList = ArrayList<Question>()
    private val unChangedQuestionList = ArrayList<Question>()
    private val _questionListLiveData = MutableLiveData<Event<List<Question>>>()
    private val _topicListLiveData = MutableLiveData<Event<List<String>>>()
    private val _practiceQuestionSessionAnswers =
        MutableLiveData<HashMap<String, Option>>(HashMap())
    private val _studyQuestionSessionAnswers =
        MutableLiveData<HashMap<String, Option>>(HashMap())
    private val _practiceQuestionSessionNumber = MutableLiveData<Int>()
    private val _studyQuestionSessionNumber = MutableLiveData<Int>()
    val searchQuestionList = MutableLiveData<List<Question>>()
    private val _searchQuestionList = MutableLiveData<Event<List<Question>>>()
    private val _scoreSubmitted = MutableLiveData<Event<Boolean>>()

    val practiceQuestionSessionAnswers: LiveData<HashMap<String, Option>>
        get() = _practiceQuestionSessionAnswers

    val studyQuestionSessionAnswers: LiveData<HashMap<String, Option>>
        get() = _studyQuestionSessionAnswers

    val practiceQuestionSessionNumber: LiveData<Int>
        get() = _practiceQuestionSessionNumber

    val scoresSubmitted : LiveData<Event<Boolean>>
    get() = _scoreSubmitted

    val currentQuestion: LiveData<Event<Question>>
        get() = _currentQuestion

    val questionListLiveData: LiveData<Event<List<Question>>>
        get() = _questionListLiveData

    val topicListLiveData: LiveData<Event<List<String>>>
        get() = _topicListLiveData

    val currentQuestionNumber: LiveData<Int>
        get() = _currentQuestionNumber

    val totalQuestionNumber: LiveData<Event<Int>>
        get() = _totalQuestionNumber

    val searchQuestionListEvent: LiveData<Event<List<Question>>>
        get() = _searchQuestionList


    val questionTypePickedWithHash: LiveData<Event<QuestionTypeWithHash>>
        get() = _questionTypePickedWithHash

    val questionType: LiveData<Event<QuestionType>>
        get() = _questionType

    val searchTerm = FormField<String>(required = true)
        .apply {
            addValidator { validateNotEmpty() }
        }

    fun onUserPickedQuestionOptions(id: String, option: Option) {
        if (_practiceQuestionSessionAnswers.value?.contains(id) == true) {
            _practiceQuestionSessionAnswers.value?.put(id, option)
        } else {
            _practiceQuestionSessionAnswers.value?.put(id, option)
        }
        _practiceQuestionSessionNumber.value = _practiceQuestionSessionAnswers.value?.size
    }

    fun onUserPickedStudyQuestionOptions(id: String, option: Option) {
        if (_studyQuestionSessionAnswers.value?.contains(id) == true) {
            _studyQuestionSessionAnswers.value?.put(id, option)
        } else {
            _studyQuestionSessionAnswers.value?.put(id, option)
        }
        _studyQuestionSessionNumber.value = _practiceQuestionSessionAnswers.value?.size
    }

    fun clearAnswers() {
        _practiceQuestionSessionAnswers.value = HashMap()
    }

    fun clearStudyQuestionAnswer() {
        _studyQuestionSessionAnswers.value = HashMap()
    }

    fun updateQuestionType(questionTypeWithHash: QuestionTypeWithHash) {
        _questionTypePickedWithHash.value = Event(questionTypeWithHash)
    }

    fun updateQuestionType(questionTypeWithHash: QuestionType) {
        _questionType.value = Event(questionTypeWithHash)
    }


    fun updateCurrentQuestion(index: Int) {
        _currentQuestion.value = Event(questionList[index - 1])
        _currentQuestionNumber.value = index
    }

    fun getQuestionsIfAvailableFromPrefs(
        professionKey: String,
        isPracticeQuestion: Boolean = false
    ) {

        if (sharedPrefs.getQuestionType() != _questionTypePickedWithHash.value?.peekContent()?.questionType?.name){
            clearQuestionsAndSessions()
        }

        if (isPracticeQuestion) {
            if (sharedPrefs.getCurrentSessionDetails() != null && sharedPrefs.getCurrentSession() != null) {
                _questionListLiveData.value =
                    Event(updateQuestionsWithBookMarked(sharedPrefs.getCurrentSession()!!.questionList))
                _practiceQuestionSessionAnswers.value =
                    sharedPrefs.getCurrentSessionDetails()!!.currentAnswers
                questionList.addAll(updateQuestionsWithBookMarked(sharedPrefs.getCurrentSession()!!.questionList))
                setCurrentQuestion = true
            } else {
                getQuestions(professionKey, isPracticeQuestion)
            }
        } else {
            if (sharedPrefs.getCurrentStudySessionDetails() != null && sharedPrefs.getCurrentStudySession() != null) {
                _questionListLiveData.value =
                    Event(updateQuestionsWithBookMarked(sharedPrefs.getCurrentStudySession()!!.questionList))
                if (isPracticeQuestion) {
                    _practiceQuestionSessionAnswers.value =
                        sharedPrefs.getCurrentStudySessionDetails()!!.currentAnswers
                } else {
                    _studyQuestionSessionAnswers.value
                }
                questionList.addAll(updateQuestionsWithBookMarked(sharedPrefs.getCurrentStudySession()!!.questionList))
                unChangedQuestionList.addAll(updateQuestionsWithBookMarked(sharedPrefs.getCurrentStudySession()!!.questionList))
                setCurrentQuestion = true
            } else {
                setCurrentQuestion = false
                getQuestions(professionKey, isPracticeQuestion)
            }
        }
    }

    fun getQuestions(professionKey: String, isPracticeQuestion: Boolean = false) {

        Timber.e("getQuestions")
        if (viewModelScope != null) {
            Timber.e("viewModelScope Not null")
        } else {
            Timber.e("viewModelScope null")
        }

        setCurrentQuestion = false
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
                    sharedPrefs.saveQuestionType(_questionTypePickedWithHash.value?.peekContent()?.questionType?.name ?: "")
                    if (isPracticeQuestion) {
                        if (!sharedPrefs.getIfUserWantsPracticeQuestionsShuffled()) {
                            if (r.result.questions.size >= 10) {
                                questionList.addAll(
                                    updateQuestionsWithBookMarked(
                                        r.result.questions.subList(
                                            0,
                                            9
                                        )
                                    )
                                )
                                _questionListLiveData.value = Event(
                                    updateQuestionsWithBookMarked(
                                        r.result.questions.subList(
                                            0,
                                            9
                                        )
                                    )
                                )
                            } else {
                                questionList.addAll(updateQuestionsWithBookMarked(r.result.questions))
                                _questionListLiveData.value =
                                    Event(updateQuestionsWithBookMarked(r.result.questions))
                            }
                        } else {
                            questionList.addAll(
                                shuffleQuestions(
                                    updateQuestionsWithBookMarked(r.result.questions),
                                    true
                                )
                            )
                            _questionListLiveData.value = Event(
                                shuffleQuestions(
                                    updateQuestionsWithBookMarked(r.result.questions),
                                    true
                                )
                            )
                        }
                    } else {
                        unChangedQuestionList.addAll(updateQuestionsWithBookMarked(r.result.questions))
                        if (!sharedPrefs.getIfUserWantsStudyQuestionsShuffled()) {
                            questionList.addAll(updateQuestionsWithBookMarked(r.result.questions))
                            _questionListLiveData.value =
                                Event(updateQuestionsWithBookMarked(r.result.questions))
                        } else {
                            questionList.addAll(
                                shuffleQuestions(
                                    updateQuestionsWithBookMarked(r.result.questions),
                                    false
                                )
                            )
                            _questionListLiveData.value = Event(
                                shuffleQuestions(
                                    updateQuestionsWithBookMarked(r.result.questions),
                                    false
                                )
                            )
                        }

                        _topicListLiveData.value = Event(getAllUniqueTopics(questionList))
                    }
                    _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    sharedPrefs.saveQuestionType(_questionTypePickedWithHash.value?.peekContent()?.questionType?.name ?: "")
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }
    }

    fun searchQuestions() {
        if (!searchTerm.verify()) return
        viewModelScope.launch {
            _loadingStatus.value =
                LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
            val token = prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).tokenData.token
            val bearer = "Bearer ".plus(token)
            when (val result = questionRepository.searchQuestions(bearer, searchTerm.value!!)) {
                is Result.Success -> {
                    val updatedQuestions = updateQuestionsWithBookMarked(result.result.questions)
                    _loadingStatus.value = LoadingStatus.Success
                    searchQuestionList.value = updatedQuestions
                    _searchQuestionList.value = Event(updatedQuestions)
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }
        }
    }

    fun submitScores(totalNumber: Int, score: Int){
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
            val token = prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).tokenData.token
            val bearer = "Bearer ".plus(token)
            when (val result = authRepository.submitScore(
                bearerAndToken = bearer,
                postUsersScoreRequest = PostUsersScoreRequest(
                    totalScore = totalNumber,
                    correctScore = score
                )
            )) {
                is Result.Success -> {
                    _scoreSubmitted.value = Event(true)
                    _loadingStatus.value = LoadingStatus.Success
                }

                is Result.Error -> {
                    _scoreSubmitted.value = Event(false)
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }

        }
    }

    private fun shuffleQuestions(
        questions: List<Question>,
        isPracticeQuestion: Boolean
    ): List<Question> {
        val questionList = ArrayList<Question>()

        val start = 0
        var end: Int = if (isPracticeQuestion) {
            if (questions.size >= 10) 9 else questions.lastIndex
        } else {
            questions.lastIndex
        }

        for (i in start..end) {
            val ranInt = (start..end).random()
            Timber.e("random Int ====$start ===== $ranInt ===== end $end ======= size ${questions.size}")
            questionList.add(questions[ranInt])
        }

        return questionList
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.addAll(
            arrayListOf(
                currentQuestionNumber,
                questionListLiveData,
                currentQuestion,
                totalQuestionNumber,
                questionTypePickedWithHash
            )
        )
    }

    fun updateTotalQuestionsNumber(totalNumber: Int) {
        _totalQuestionNumber.value = Event(totalNumber)
    }

    override fun onCleared() {
        Timber.e("view model has been cleared")
        super.onCleared()
    }

    private fun updateQuestionsWithBookMarked(questionList: List<Question>): List<Question> {
       // sharedPrefs.deletePref(PrefKeys.BOOKMARKED_QUESTIONS)
        val questions = ArrayList<Question>()
        val hashMap: HashMap<String, Question> = java.util.HashMap()
        val bookMarkedQuestions = sharedPrefs.getSavedBookMarked()
        for (question in bookMarkedQuestions.questionList) {
            hashMap.put(question.id, question)
        }

        for (question in questionList) {
            if (hashMap.containsKey(question.id)) {
                question.isBookmarked = true
            }
            questions.add(question)
        }

        return questions
    }

    private fun getAllUniqueTopics(questions: List<Question>): List<String> {
        var topicList: MutableList<String> = ArrayList()
        topicList.add(0, "All")
        var topicSet: MutableSet<String> = HashSet()
        for (question in questions) {
            topicSet.add(question.topic.name)
        }
        topicList.addAll(topicSet.toList())
        return topicList
    }

    fun getCorrectAnswersGotten(): Int {
        var totalNumber = 0
        for (keys in _practiceQuestionSessionAnswers.value!!.keys) {
            if (_practiceQuestionSessionAnswers.value!![keys]?.isCorrect == true) {
                totalNumber++
            }
        }
        Timber.e("=====total correct gotten ======= $totalNumber")
        return totalNumber
    }

    fun chooseANewTopic(topicString: String) {
        val newQuestions = filterQuestionWithTopic(unChangedQuestionList, topicString)
        _questionListLiveData.value = Event(newQuestions)
        questionList.clear()
        questionList.addAll(newQuestions)
        //updateCurrentQuestion(1)
    }

    private fun filterQuestionWithTopic(
        questions: List<Question>,
        topicString: String
    ): List<Question> {
        if (topicString == "All") {
            return questions
        } else {
            return questions.filter { it.topic.name == topicString }
        }
    }

    fun clearQuestionsAndSessions(){
        sharedPrefs.deletePref(
            PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS,
            PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS,
            PrefKeys.CURRENT_REVIEW_QUESTIONS_SESSIONS,
            PrefKeys.CURRENT_QUESTIONS_SESSIONS,
            PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS
        )
        unChangedQuestionList.clear()
        _questionListLiveData.value = Event(ArrayList())
        questionList.clear()
    }
}