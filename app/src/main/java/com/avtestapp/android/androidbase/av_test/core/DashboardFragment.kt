package com.avtestapp.android.androidbase.av_test.core

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
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
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionDialogViewModel
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionTypeFragmentDirections
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionViewModel
import com.avtestapp.android.androidbase.av_test.modals.QuestionTypeBottomSheetModal
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.DashboardFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.hideKeyboard
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import javax.inject.Inject

class DashboardFragment : BaseViewModelFragment(){

    companion object {
        fun newInstance() = DashboardFragment()
    }

    @Inject
    lateinit var prefsUtils: PrefsUtils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var binding: DashboardFragmentBinding
    private var isStudyQuestion = true
    private lateinit var viewModel: DashboardViewModel
    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var questionDialogViewModel: QuestionDialogViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private val searchChoice = arrayOf("Question", "Answer")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel() = questionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)
        questionViewModel = ViewModelProvider(activity!!).get(QuestionViewModel::class.java)
        questionDialogViewModel = ViewModelProvider(this, viewModelFactory).get(QuestionDialogViewModel::class.java)
        binding.viewModel = questionViewModel
        setUpViews()
        subscribeObservers()
    }

    private fun setUpViews(){
        dashboardAdapter = DashboardAdapter {
            Timber.e("$it")
        }

        binding.search
        if (prefsUtils.getBoolean(PrefKeys.HAS_CLICKED_ON_STUDY_QUESTIONS, false)){
            binding.resumeStudyQuestionText.text = "Resume"
        }else {
            binding.resumeStudyQuestionText.text = "Start"
        }

        if (prefsUtils.getBoolean(PrefKeys.HAS_CLICKED_ON_PRACTICE_QUESTIONS, false)){
            binding.resumePracticeQuestionText.text = "Resume"
        }else {
            binding.resumePracticeQuestionText.text = "Start"
        }
        binding.markedQuestions.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToMarkedQuestionsFragment())
        }

        binding.resumePracticeQuestion.setOnClickListener {
            isStudyQuestion = false
            mainActivity.presentModal(QuestionTypeBottomSheetModal())
        }

        binding.resumeStudyQuestion.setOnClickListener {
            isStudyQuestion = true
            mainActivity.presentModal(QuestionTypeBottomSheetModal())
        }

        binding.knowledgeScore.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToKnowledgeScoreFragment())
        }

        binding.moreMenu.setOnClickListener {
            searchOptionsDialog()
        }

        binding.search.setOnClickListener {
            questionViewModel.searchQuestions()
        }

        binding.fabButton.setOnClickListener {
            showCardView()
        }

        binding.searchTextInputEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
                questionViewModel.searchQuestions()
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    fun setSelection(searchOption: String){

    }

    private fun searchOptionsDialog(){
            val builder =
                AlertDialog.Builder(view!!.context)
            builder.setTitle("")
            builder.setItems(
                searchChoice
            ) { _: DialogInterface?, selectedIndex: Int ->
                setSelection(searchOption = searchChoice[selectedIndex])
            }
            builder.setPositiveButton(R.string.close, null)
            builder.create().show()

    }



    private fun subscribeObservers() {
        binding.searchTextInputEditText.setFeedbackSource(this, questionViewModel.searchTerm)
        questionDialogViewModel.questionTypePickedWithHash.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {questionType->
                when(questionType.questionType){
                    QuestionType.PILOT -> {
                        questionViewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.PILOT,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
                        }                     }

                    QuestionType.ENGINEER-> {
                        questionViewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.ENGINEER,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
                        }                    }

                    QuestionType.AIR_DISPATCHER -> {
                        questionViewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.AIR_DISPATCHER,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
                        }
                    }

                    QuestionType.FLIGHT_ATTENDANT -> {
                        questionViewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.FLIGHT_ATTENDANT,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
                        }
                    }

                    QuestionType.AIR_TRAFFIC_CONTROLLER -> {
                        questionViewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.AIR_TRAFFIC_CONTROLLER,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
                        }
                    }
                }
            }
        })

        questionViewModel.searchQuestionListEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.isNotEmpty()) {
                    binding.allCardsGroup.hide()
                    binding.emptyViewLinearLayout.hide()
                    binding.searchQuestionsRecyclerView.show()
                    binding.fabButton.show()
                    binding.searchQuestionsRecyclerView.adapter = dashboardAdapter
                }
                else {
                    showEmptyView()
                    //Toast.makeText(activity!!, "List is empty", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showEmptyView(){
        binding.allCardsGroup.hide()
        binding.searchQuestionsRecyclerView.hide()
        binding.emptyViewLinearLayout.show()
        binding.fabButton.show()
        binding.emptyView.textActionWord.hide()
        binding.emptyView.descriptionText.text = "No question or answer matches your search"
    }
    private fun showCardView(){
        binding.allCardsGroup.show()
        binding.emptyViewLinearLayout.hide()
        binding.searchQuestionsRecyclerView.hide()
        binding.fabButton.hide()
    }


}
