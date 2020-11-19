package com.avtestapp.android.androidbase.av_test.core.questions

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentStudyQuestionBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import javax.inject.Inject

class StudyQuestionFragment : BaseViewModelFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    private lateinit var binding: FragmentStudyQuestionBinding
    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyQuestionBinding.inflate(inflater)
        binding.lifecycleOwner
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun getViewModel() = viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(activity!!).get(QuestionViewModel::class.java)
        subscribeObservers()
        setUpView()
        prefsUtils.putBoolean(PrefKeys.HAS_CLICKED_ON_STUDY_QUESTIONS, true)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        Timber.e("it reached here on resume")
        viewModel.getQuestions(
            viewModel.questionTypePickedWithHash.value?.peekContent()?.hashString ?: ""
        )
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


        viewModel.totalQuestionNumber.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                binding.totalQuestionsNo.text = it.toString()
            }
        })

        viewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            it.peekContent()?.let {
                viewModel.updateTotalQuestionsNumber(it.size)
                mainActivity.dismissLoading()
                if (it.isEmpty()) {
                    setUpEmptyView()
                } else {
                    updateQuestionView(it[0])
                }
            }

            if(it.peekContent() == null){
                viewModel.updateTotalQuestionsNumber(0)
                setUpEmptyView()

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


    private fun setUpView() {
        //setUpEmptyView()

        binding.bookmarkIcon.setOnClickListener {
            binding.bookmarkIcon.isSelected = !binding.bookmarkIcon.isSelected
        }
        mainActivity.supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }

        binding.backButton.setOnClickListener {
            mainActivity.onBackPressed()
        }

        binding.questionOptionsPopUp.setOnClickListener {
            setUpPopupDialog()
        }

        binding.prev.setOnClickListener {
            viewModel.updateCurrentQuestion(viewModel.currentQuestionNumber.value ?: 0 - 1)
        }

        binding.next.setOnClickListener {
            viewModel.updateCurrentQuestion(viewModel.currentQuestionNumber.value ?: 0 + 1)
        }
    }

    fun setUpPopupDialog() {
        val list = prefsUtils.getList<ProfessionItemsResponse>(
            PrefKeys.PROFESSIONS_LIST,
            object : TypeToken<List<ProfessionItemsResponse>>() {}.type
        )
        val listOfString = Array(list.size) { "" }
        var i = 0
        list.forEach {
            listOfString[i++] = it.name
        }

        val builder =
            AlertDialog.Builder(view!!.context)
        builder.setTitle("")
        builder.setItems(
            listOfString
        ) { _: DialogInterface?, selectedIndex: Int ->
            setSelection(
                listOfString[selectedIndex],
                selectedIndex
            )
        }
        builder.setPositiveButton(R.string.close, null)
        builder.create().show()
    }

    private fun setSelection(item: String, selectionIndex: Int) {
        binding.studyQuestionsToolbarText.text = item
    }

    private fun setUpEmptyView() {
        binding.totalQuestionGroup.hide()
        binding.emptylayout.emptylayout.show()
        binding.emptylayout.descriptionText.text = "No study question to resume"
        binding.emptylayout.textActionWord.text = ""
    }


}