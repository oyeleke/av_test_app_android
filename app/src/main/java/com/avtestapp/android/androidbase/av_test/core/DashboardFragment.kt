package com.avtestapp.android.androidbase.av_test.core

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionViewModel
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.DashboardFragmentBinding
import com.avtestapp.android.androidbase.utils.PrefsUtils
import javax.inject.Inject

class DashboardFragment : BaseViewModelFragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    @Inject
    lateinit var prefsUtils: PrefsUtils

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: DashboardFragmentBinding

    private lateinit var viewModel: DashboardViewModel

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

    override fun getViewModel() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)
        setUpViews()
    }

    private fun setUpViews(){
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
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToMarkedQuestionFragment())
        }

        binding.resumePracticeQuestion.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToPracticeQuestionsFragment())
        }

        binding.resumeStudyQuestion.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToStudyQuestionFragment())
        }

        binding.knowledgeScore.setOnClickListener {

        }
    }

}