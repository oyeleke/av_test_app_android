package com.avtestapp.android.androidbase.av_test.core.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionDetails
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionQuestions
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentPracticeQuestionsBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.avtestapp.android.androidbase.utils.PrefsUtils
import timber.log.Timber
import javax.inject.Inject


class PracticeQuestionsFragment : BaseViewModelFragment() {

    lateinit var binding: FragmentPracticeQuestionsBinding
    lateinit var currentQuestion: Question
    var totalQuestions = 0
    var isSubmit = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: QuestionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPracticeQuestionsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(activity!!).get(QuestionViewModel::class.java)
        subscribeObservers()
        setUpView()
        viewModel.clearAnswers()
        prefsUtils.putBoolean(PrefKeys.HAS_CLICKED_ON_PRACTICE_QUESTIONS, true)
        viewModel.getQuestionsIfAvailableFromPrefs(
            viewModel.questionTypePickedWithHash.value?.peekContent()?.hashString ?: "", true
        )
    }

    override fun onStart() {
        super.onStart()

    }

    private fun setUpView() {
        mainActivity.supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }

        binding.reviewFailedQuestions.setOnClickListener {
            findNavController().navigate(PracticeQuestionsFragmentDirections.actionPracticeQuestionsFragmentToReviewQuestionsFragment())
        }


        binding.backButton.setOnClickListener {
            mainActivity.onBackPressed()
        }

        binding.prev.setOnClickListener {
            if (viewModel.currentQuestionNumber.value ?: 0 > 0) {
                viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: 0) - 1)
            }
        }

        binding.next.setOnClickListener {
            if (!isSubmit) {
                viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: -1) + 1)
            } else {
                val stats = returnPercentage()
                viewModel.submitScores(totalQuestions, totalQuestions - stats.second)
                //showResultView()
            }
        }

        binding.answerOption1.setOnClickListener {
            viewModel.onUserPickedQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(0)
            )
            binding.answerOption1.isSelected = true
            binding.answerOption2.isSelected = false
            binding.answerOption3.isSelected = false
        }

        binding.answerOption2.setOnClickListener {
            viewModel.onUserPickedQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(1)
            )
            binding.answerOption1.isSelected = false
            binding.answerOption2.isSelected = true
            binding.answerOption3.isSelected = false
        }

        binding.answerOption3.setOnClickListener {
            viewModel.onUserPickedQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(2)
            )
            binding.answerOption1.isSelected = false
            binding.answerOption2.isSelected = false
            binding.answerOption3.isSelected = true
        }

        binding.retakeQuestions.setOnClickListener {
            resetPracticeQuestions()
        }

        binding.bookmarkIcon.setOnClickListener {
            binding.bookmarkIcon.isSelected = !binding.bookmarkIcon.isSelected
            val savedQuestions = sharedPrefs.getSavedBookMarked()
            if (savedQuestions.questionList.find { it.id == currentQuestion.id} !=  null){
                savedQuestions.questionList.remove(currentQuestion)
            } else {
                currentQuestion.isPracticeQuestion = true
                savedQuestions.questionList.add(currentQuestion)
                Toast.makeText(activity!!, "Question Saved", Toast.LENGTH_SHORT).show()
            }
            sharedPrefs.saveBookmarkedQuestions(savedQuestions)
        }

        //setUpEmptyView()
    }

    private fun subscribeObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                binding.answerOption1.isSelected = false
                binding.answerOption2.isSelected = false
                binding.answerOption3.isSelected = false
                updateQuestionView(it)
                currentQuestion = it
                binding.bookmarkIcon.isSelected = it.isBookmarked
            }

        })

        viewModel.currentQuestionNumber.observe(viewLifecycleOwner, Observer {
            val questionNo = if (it in 0..9) "0${it}" else "$it"
            binding.questionNoTextView.text = questionNo

            sharedPrefs.saveCurrentSessionDetails(
                CurrentSessionDetails(
                    it,
                    viewModel.practiceQuestionSessionAnswers.value!!
                )
            )
            if (it == 1) {
                binding.prev.hide()
            } else {
                binding.prev.show()

            }

            if (it == viewModel.totalQuestionNumber.value!!.peekContent()) {
                //binding.next.hide()
                isSubmit = true
                binding.nextText.text = "SUBMIT"
            } else {
                isSubmit = false
                binding.nextText.text = "NEXT"
                binding.next.show()
            }
        })

        viewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            it.peekContent()?.let {

                sharedPrefs.saveCurrentQuestionsForReview(CurrentSessionQuestions(it))
                viewModel.updateTotalQuestionsNumber(it.size)

                sharedPrefs.saveCurrentSession(CurrentSessionQuestions(it))
                mainActivity.dismissLoading()
                if (it.isEmpty()) {
                    setUpEmptyView()
                } else {
                    hideEmptyView()
                    if (viewModel.setCurrentQuestion){
                        viewModel.updateCurrentQuestion(sharedPrefs.getCurrentSessionDetails()!!.currentQuestion)
                    }else {
                        viewModel.updateCurrentQuestion(1)
                    }
                }
            }

            if (it.peekContent() == null) {
                viewModel.updateTotalQuestionsNumber(0)
                setUpEmptyView()

            }
        })

        viewModel.totalQuestionNumber.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                binding.totalQuestionsNo.text = it.toString()
                totalQuestions = it
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoadingStatus.Error -> {
                    mainActivity.dismissLoading()
                }
            }
        })

        viewModel.scoresSubmitted.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it){
                    showResultView()
                }
            }
        })

    }

    private fun hideEmptyView(){
        binding.bottomQuestionNav.show()
        binding.totalQuestionGroup.show()
        binding.emptylayout.emptylayout.hide()
    }

    private fun setUpEmptyView() {
        binding.bottomQuestionNav.hide()
        binding.totalQuestionGroup.hide()
        binding.emptylayout.emptylayout.show()
        binding.emptylayout.descriptionText.text = "No practice questions to resume"
        binding.emptylayout.textActionWord.text = ""
    }

    private fun updateQuestionView(question: Question) {

        binding.questionString.text = question.text
        if (question.options.isNotEmpty()) {
            binding.answerOption1Text.text = question.options[0].text
        } else {
            binding.answerOption1.hide()
        }

        if (question.options.size > 1) {
            binding.answerOption2Text.text = question.options[1].text
        } else {
            binding.answerOption2.hide()
        }

        if (question.options.size > 2) {
            binding.answerOption3Text.text = question.options[2].text
        } else {
            binding.answerOption3.hide()
        }
        var answers = viewModel.practiceQuestionSessionAnswers.value
        if ( answers != null){
            if (answers.containsKey(question.id)){
                val option = answers.get(question.id)
                if (question.options.isNotEmpty()) {
                    if (question.options[0].id == option?.id) {
                        binding.answerOption1.isSelected = true
                    }
                }
                if (question.options.size > 1) {
                    if (question.options[1].id == option?.id) {
                        binding.answerOption2.isSelected = true
                    }
                }
                if (question.options.size > 2) {
                    if (question.options[2].id == option?.id) {
                        binding.answerOption3.isSelected = true
                    }
                }

            }
        }
    }

    private fun showResultView() {
        binding.questionsContainerView.hide()
        binding.resultsLayout.show()
        //sharedPrefs.deletePref(PrefKeys.CURRENT_QUESTIONS_SESSIONS, PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS)
        val stats = returnPercentage()
        binding.percentScores.text = getString(R.string.percentage_score, "${stats.first}%")
        binding.missedQuestions.text =
            getString(R.string.you_missed_05_50, "${stats.second}/${totalQuestions}")
    }

    private fun returnPercentage(): Pair<Int, Int> {
        val questionsGotten = viewModel.getCorrectAnswersGotten()
        val percent: Double = (questionsGotten.toDouble() / totalQuestions.toDouble()) * 100
        val missedQuestions = totalQuestions - questionsGotten
        Timber.e("=====total questions ======= $totalQuestions")
        return Pair(first = percent.toInt(), second = missedQuestions)
    }

    private fun resetPracticeQuestions() {
        binding.questionsContainerView.show()
        binding.resultsLayout.hide()
        sharedPrefs.saveCurrentSession(CurrentSessionQuestions(viewModel.questionListLiveData.value!!.peekContent()))
        viewModel.updateCurrentQuestion(1)
        viewModel.clearAnswers()
    }

    override fun getViewModel() = viewModel

}