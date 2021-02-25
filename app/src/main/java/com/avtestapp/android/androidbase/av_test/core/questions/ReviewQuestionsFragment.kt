package com.avtestapp.android.androidbase.av_test.core.questions

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.ReviewQuestionsFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.avtestapp.android.androidbase.utils.PrefsUtils
import javax.inject.Inject

class ReviewQuestionsFragment : BaseViewModelFragment() {

    private lateinit var binding: ReviewQuestionsFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: ReviewQuestionsViewModel

    private lateinit var currentQuestion: Question
    var endReview = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReviewQuestionsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun getViewModel() = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ReviewQuestionsViewModel::class.java)
        viewModel.getLastQuestionsAnswered()
        subscribeObservers()
        setupView()
    }

    private fun setupView() {
        binding.prev.setOnClickListener {
            if (viewModel.currentQuestionNumber.value ?: 0 > 0) {
                viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: 0) - 1)
            }
        }

        binding.next.setOnClickListener {
            if (!endReview) {
                viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: -1) + 1)
            }else {
                sharedPrefs.deletePref(PrefKeys.CURRENT_QUESTIONS_SESSIONS, PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS)
                findNavController().navigate(R.id.dashboardFragment)
            }
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

        binding.chatIcon.setOnClickListener {
            setUpExplanationDialog(currentQuestion.explanation!!)
        }

    }


    private fun subscribeObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                currentQuestion = it
                
                binding.answerOption1.background = ContextCompat.getDrawable(activity!!, R.drawable.review_answer_background)
                binding.answerOption2.background = ContextCompat.getDrawable(activity!!, R.drawable.review_answer_background)
                binding.answerOption3.background = ContextCompat.getDrawable(activity!!, R.drawable.review_answer_background)

                if (it.options.isNotEmpty()) {
                    if (it.options[0].isCorrect) {
                        binding.answerOption1.isSelected = true
                        binding.answerOption2.isSelected = false
                        binding.answerOption3.isSelected = false
                    }
                }
                if (it.options.size > 1) {
                    if (it.options[1].isCorrect) {
                        binding.answerOption1.isSelected = false
                        binding.answerOption2.isSelected = true
                        binding.answerOption3.isSelected = false
                    }
                }
                if (it.options.size > 2) {
                    if (it.options[2].isCorrect) {
                        binding.answerOption1.isSelected = false
                        binding.answerOption2.isSelected = false
                        binding.answerOption3.isSelected = true
                    }
                }
                binding.bookmarkIcon.isSelected = it.isBookmarked

                if (it.explanation.isNullOrEmpty()){
                    binding.chatIcon.hide()
                }

                updateQuestionView(it)
            }

        })

        viewModel.currentQuestionNumber.observe(viewLifecycleOwner, Observer {
            val questionNo = if (it in 0..9) "0${it}" else "$it"
            binding.questionNoTextView.text = questionNo


            if (it == 1) {
                binding.prev.hide()
            } else {
                binding.prev.show()

            }

            if (it == viewModel.totalQuestionNumber.value!!.peekContent()) {
                //binding.next.hide()
                endReview = true
                binding.nextText.text = "END REVIEW"
            } else {
                endReview = false
                binding.nextText.text = "NEXT"
                binding.next.show()
            }
        })

        viewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            it.peekContent()?.let {
                viewModel.updateTotalQuestionsNumber(it.size)
                viewModel.updateCurrentQuestion(1)
                mainActivity.dismissLoading()
                updateQuestionView(it[0])
            }
        })

        viewModel.totalQuestionNumber.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                binding.totalQuestionsNo.text = it.toString()
            }
        })
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
                        if (!option.isCorrect){
                            binding.answerOption1.background = ContextCompat.getDrawable(activity!!, R.color.colorRed)
                        }
                    }
                }

                if (question.options.size > 1) {
                    if (question.options[1].id == option?.id) {
                        if (!option.isCorrect){
                            binding.answerOption2.background = ContextCompat.getDrawable(activity!!, R.color.colorRed)
                        }
                    }
                }

                if (question.options.size > 2) {
                    if (question.options[2].id == option?.id) {
                        if (!option.isCorrect){
                            binding.answerOption3.background = ContextCompat.getDrawable(activity!!, R.color.colorRed)
                        }
                    }
                }

            }
        }
    }



    fun setUpExplanationDialog(explanation: String) {

        val builder =
            AlertDialog.Builder(view!!.context)
        builder.setTitle("Explanation")
        builder.setMessage(explanation)
        builder.setPositiveButton(R.string.close, null)
        builder.create().show()
    }
}