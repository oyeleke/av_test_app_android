package com.avtestapp.android.androidbase.av_test.core.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentPracticeQuestionsBinding
import com.avtestapp.android.androidbase.databinding.FragmentStudyQuestionBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.utils.PrefsUtils
import javax.inject.Inject


class PracticeQuestionsFragment : BaseViewModelFragment() {

    lateinit var binding: FragmentPracticeQuestionsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

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
        prefsUtils.putBoolean(PrefKeys.HAS_CLICKED_ON_PRACTICE_QUESTIONS, true)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getQuestions(viewModel.questionTypePickedWithHash.value?.peekContent()?.hashString?:"")
    }

    private fun setUpView(){
        mainActivity.supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator( R.drawable.ic_arrow_back_white_24dp)
        }

        binding.bookmarkIcon.setOnClickListener {
            binding.bookmarkIcon.isSelected = !binding.bookmarkIcon.isSelected
        }

        binding.backButton.setOnClickListener {
            mainActivity.onBackPressed()
        }

        binding.prev.setOnClickListener {
            viewModel.updateCurrentQuestion(viewModel.currentQuestionNumber.value!! - 1)
        }

        binding.next.setOnClickListener {
            viewModel.updateCurrentQuestion(viewModel.currentQuestionNumber.value!! + 1)
        }
        //setUpEmptyView()
    }

    private fun subscribeObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                updateQuestionView(it)
            }

        })

        viewModel.currentQuestionNumber.observe(viewLifecycleOwner, Observer {
            val questionNo = if (it in 0..9) "0${it}" else "$it"
            binding.questionNoTextView.text = questionNo

            if (it == 1){
                binding.prev.hide()
            } else {
                binding.prev.show()

            }

            if (it == viewModel.totalQuestionNumber.value!!.peekContent()){
                binding.next.hide()
            }else {
                binding.next.show()
            }
        })

        viewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                mainActivity.dismissLoading()
                if (it.isEmpty()) {
                    setUpEmptyView()
                } else {
                    updateQuestionView(it[0])
                }
            }
        })

        viewModel.totalQuestionNumber.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                binding.totalQuestionsNo.text = it.toString()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                is LoadingStatus.Error -> {
                    mainActivity.dismissLoading()
                }
            }
        })
    }

    private fun setUpEmptyView(){
        binding.totalQuestionGroup.hide()
        binding.emptylayout.emptylayout.show()
        binding.emptylayout.descriptionText.text = "No practice questions to resume"
        binding.emptylayout.textActionWord.text = ""
    }

    private fun updateQuestionView(question: Question) {

        binding.questionString.text = question.text
        if (question.options.isNotEmpty()){
            binding.answerOption1Text.text = question.options[0].text
        } else {
            binding.answerOption1.hide()
        }

        if (question.options.size > 1){
            binding.answerOption2Text.text = question.options[1].text
        } else {
            binding.answerOption2.hide()
        }

        if (question.options.size > 2){
            binding.answerOption3Text.text = question.options[2].text
        } else {
            binding.answerOption3.hide()
        }
    }

    override fun getViewModel() = viewModel

}