package com.avtestapp.android.androidbase.av_test.core.questions

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionDetails
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionQuestions
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentStudyQuestionBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import javax.inject.Inject

class StudyQuestionFragment : BaseViewModelFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var binding: FragmentStudyQuestionBinding
    private lateinit var viewModel: QuestionViewModel
    lateinit var currentQuestion: Question
    private lateinit var listOfTopics : Array<String>


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
        viewModel.getQuestions(
            viewModel.questionTypePickedWithHash.value?.peekContent()?.hashString ?: ""
        )
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        Timber.e("it reached here on resume")

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
            viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: 0 )- 1)
        }

        binding.next.setOnClickListener {
            viewModel.updateCurrentQuestion((viewModel.currentQuestionNumber.value ?: 0 )+ 1)
        }

        binding.chatIcon.setOnClickListener {
            setUpExplanationDialog(currentQuestion.explanation!!)
        }

        binding.answerOption1.setOnClickListener {
            viewModel.onUserPickedStudyQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(0)
            )
            binding.answerOption1.isSelected = true
            binding.answerOption2.isSelected = false
            binding.answerOption3.isSelected = false
        }

        binding.answerOption2.setOnClickListener {
            viewModel.onUserPickedStudyQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(1)
            )
            binding.answerOption1.isSelected = false
            binding.answerOption2.isSelected = true
            binding.answerOption3.isSelected = false
        }

        binding.answerOption3.setOnClickListener {
            viewModel.onUserPickedStudyQuestionOptions(
                currentQuestion.id,
                currentQuestion.options.get(2)
            )
            binding.answerOption1.isSelected = false
            binding.answerOption2.isSelected = false
            binding.answerOption3.isSelected = true
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

    }

    private fun subscribeObservers() {
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer {

            it.getContentIfNotHandled()?.let {
                currentQuestion = it

                if (sharedPrefs.getShowOnlyAnswersForStudyQuestions()) {
                    if (it.options.isNotEmpty()) {
                        if (it.options[0].isCorrect) {
                            binding.answerOption1.show()
                            binding.answerOption2.hide()
                            binding.answerOption3.hide()
                        }
                    }
                    if (it.options.size > 1) {
                        if (it.options[1].isCorrect) {
                            binding.answerOption1.hide()
                            binding.answerOption2.show()
                            binding.answerOption3.hide()
                        }
                    }
                    if (it.options.size > 2) {
                        if (it.options[2].isCorrect) {
                            binding.answerOption1.hide()
                            binding.answerOption2.hide()
                            binding.answerOption3.show()
                        }
                    }
                } else {
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
                updateQuestionView(it)
                binding.bookmarkIcon.isSelected = it.isBookmarked

                if (it.explanation.isNullOrEmpty()){
                    binding.chatIcon.hide()
                }
            }

        })

        viewModel.currentQuestionNumber.observe(viewLifecycleOwner, Observer {
            val questionNo = if (it in 0..9) "0${it}" else "$it"
            binding.questionNoTextView.text = questionNo

            sharedPrefs.saveCurrentStudySessionDetails(
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
                sharedPrefs.saveCurrentStudySession(CurrentSessionQuestions(it))
                if (it.isEmpty()) {
                    setUpEmptyView()
                } else {
                    if (viewModel.setCurrentQuestion){
                        viewModel.updateCurrentQuestion(sharedPrefs.getCurrentSessionDetails()!!.currentQuestion)
                    }else {
                        viewModel.updateCurrentQuestion(1)
                    }
                }
            }

            if(it.peekContent() == null){
                viewModel.updateTotalQuestionsNumber(0)
                setUpEmptyView()

            }
        })

        viewModel.topicListLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                listOfTopics = it.toTypedArray()
                binding.studyQuestionsToolbarText.text = listOfTopics[0]

            }
        })

//        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is LoadingStatus.Error -> {
//                    mainActivity.dismissLoading()
//                }
//            }
//        })
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

        var answers = viewModel.studyQuestionSessionAnswers.value
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
            listOfTopics
        ) { _: DialogInterface?, selectedIndex: Int ->
            setSelection(
                listOfTopics[selectedIndex],
                selectedIndex
            )
        }
        builder.setPositiveButton(R.string.close, null)
        builder.create().show()
    }

    fun setUpExplanationDialog(explanation: String) {

        val builder =
            AlertDialog.Builder(view!!.context)
        builder.setTitle("Explanation")
        builder.setMessage(explanation)
        builder.setPositiveButton(R.string.close, null)
        builder.create().show()
    }

    private fun setSelection(item: String, selectionIndex: Int) {
        binding.studyQuestionsToolbarText.text = item
        viewModel.chooseANewTopic(item)
    }



    private fun setUpEmptyView() {
        binding.bottomQuestionNav.hide()
        binding.totalQuestionGroup.hide()
        binding.emptylayout.emptylayout.show()
        binding.emptylayout.descriptionText.text = "No study question to resume"
        binding.emptylayout.textActionWord.text = ""
    }


}