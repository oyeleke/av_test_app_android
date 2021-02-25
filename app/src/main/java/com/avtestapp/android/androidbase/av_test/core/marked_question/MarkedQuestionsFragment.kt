package com.avtestapp.android.androidbase.av_test.core.marked_question

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.MarkedQuestionsFragmentBinding
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import javax.inject.Inject

class MarkedQuestionsFragment : BaseViewModelFragment() {

    private lateinit var binding: MarkedQuestionsFragmentBinding
    private lateinit var tabAdapter: TabAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: MarkedQuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarkedQuestionsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun getViewModel() = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(activity!!).get(MarkedQuestionsViewModel::class.java)
        setupView()
    }

    private fun setupView(){
        tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(MarkedStudyQuestionFragment(), "STUDY QUESTION")
        tabAdapter.addFragment(MarkedPracticeQuestionFragment(), "PRACTICE QUESTION")
        binding.viewpager.adapter = tabAdapter
        binding.tabs.setupWithViewPager(binding.viewpager)

        binding.backButton.setOnClickListener {
            mainActivity.onBackPressed()
        }

    }

}