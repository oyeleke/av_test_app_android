package com.avtestapp.android.androidbase.av_test.core.questions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.UI_CONSTANTS
import com.avtestapp.android.androidbase.av_test.modals.QuestionTypeBottomSheetModal
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.FragmentQuestionsTypeBinding
import javax.inject.Inject


class QuestionTypeFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var isStudyQuestion = true
    private lateinit var viewModel: QuestionViewModel
    private lateinit var binding: FragmentQuestionsTypeBinding
    private lateinit var questionDialogViewModel: QuestionDialogViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionsTypeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionViewModel::class.java)
        viewModel = ViewModelProvider(activity!!).get(QuestionViewModel::class.java)
        questionDialogViewModel = ViewModelProvider(this, viewModelFactory).get(QuestionDialogViewModel::class.java)
        subscribeObservers()
        setUpViews()
    }

    private fun subscribeObservers() {
        questionDialogViewModel.questionTypePickedWithHash.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {questionType->
                when(questionType.questionType){
                     QuestionType.PILOT -> {
                         viewModel.updateQuestionType(
                             QuestionTypeWithHash(
                                 questionType = QuestionType.PILOT,
                                 hashString = questionType.hashString
                             )
                         )
                         if (isStudyQuestion){
                             findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
                         } else {
                             findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToPracticeQuestionsFragment())
                         }                     }

                    QuestionType.ENGINEER-> {
                        viewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.ENGINEER,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToPracticeQuestionsFragment())
                        }                    }

                    QuestionType.AIR_DISPATCHER -> {
                        viewModel.updateQuestionType(
                            QuestionTypeWithHash(
                            questionType = QuestionType.AIR_DISPATCHER,
                            hashString = questionType.hashString
                        )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToPracticeQuestionsFragment())
                        }
                    }

                    QuestionType.FLIGHT_ATTENDANT -> {
                        viewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.FLIGHT_ATTENDANT,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToPracticeQuestionsFragment())
                        }
                    }

                    QuestionType.AIR_TRAFFIC_CONTROLLER -> {
                        viewModel.updateQuestionType(
                            QuestionTypeWithHash(
                                questionType = QuestionType.AIR_TRAFFIC_CONTROLLER,
                                hashString = questionType.hashString
                            )
                        )
                        if (isStudyQuestion){
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
                        } else {
                            findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToPracticeQuestionsFragment())
                        }
                    }
                }
            }
        })
    }

    private fun setUpViews() {
        mainActivity.setUpToolBar("Select a Path", isRootPage = true)

        binding.practiceQuestionView.setOnClickListener {
            isStudyQuestion = false
            mainActivity.presentModal(QuestionTypeBottomSheetModal())
        }

        binding.studyQuestionView.setOnClickListener {
            isStudyQuestion = true
            mainActivity.presentModal(QuestionTypeBottomSheetModal())
            //findNavController().navigate(QuestionTypeFragmentDirections.actionQuestionTypeFragmentToStudyQuestionFragment())
        }
    }

    override fun getViewModel() = viewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                val hash = data?.getStringExtra("hash")
                val type = data?.getStringExtra("type")
            }
        }
    }

}