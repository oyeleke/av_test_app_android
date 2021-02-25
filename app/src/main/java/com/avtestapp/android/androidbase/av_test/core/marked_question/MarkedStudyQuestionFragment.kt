package com.avtestapp.android.androidbase.av_test.core.marked_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentMarkedStudyQuestionsBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import javax.inject.Inject


class MarkedStudyQuestionFragment : BaseViewModelFragment() {

    lateinit var binding: FragmentMarkedStudyQuestionsBinding

    lateinit var currentQuestion: Question


    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: MarkedQuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarkedStudyQuestionsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(activity!!).get(MarkedQuestionsViewModel::class.java)
        viewModel.getQuestionsStudy(false)
        subscribeObservers()
        setupView()
    }

    override fun getViewModel() = viewModel

    private fun setupView(){
        binding.prev.setOnClickListener {
            if (viewModel.currentQuestionNumber.value ?: 0 > 0) {
                viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: 0) - 1)
            }
        }

        binding.next.setOnClickListener {
            viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: -1) + 1)
        }

        binding.bookmarkIcon.setOnClickListener {
            binding.bookmarkIcon.isSelected = !binding.bookmarkIcon.isSelected
            val savedQuestions = sharedPrefs.getSavedBookMarked()
            if (savedQuestions.questionList.find { it.id == currentQuestion.id} !=  null){
                savedQuestions.questionList.remove(currentQuestion)
            } else {
                currentQuestion.isPracticeQuestion = true
                savedQuestions.questionList.add(currentQuestion)
            }
            sharedPrefs.saveBookmarkedQuestions(savedQuestions)
        }
    }

    private fun subscribeObservers(){
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                currentQuestion = it
                updateQuestionView(currentQuestion)
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
                binding.next.hide()
            } else {
                binding.next.show()
            }
        })

        viewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            it.peekContent()?.let {
                mainActivity.dismissLoading()
                if (!viewModel.isPracticeQuestion) {
                    if (it.isEmpty()) {
                        setUpEmptyView()
                    } else {
                        viewModel.updateTotalQuestionsNumber(it.size)
                        viewModel.updateCurrentQuestion(1)
                        updateQuestionView(it[0])
                    }
                }
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

    }


    private fun setUpEmptyView(){
        binding.totalQuestionGroup.hide()
        binding.bottomQuestionNav.hide()
        binding.emptylayout.emptylayout.show()
        binding.emptylayout.descriptionText.text = "You’ve not marked any question"
        binding.emptylayout.textActionWord.text = "Get Started"
    }


}