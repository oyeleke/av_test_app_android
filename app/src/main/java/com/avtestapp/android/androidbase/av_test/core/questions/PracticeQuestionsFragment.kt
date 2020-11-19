package com.avtestapp.android.androidbase.av_test.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.databinding.FragmentPracticeQuestionsBinding


class PracticeQuestionsFragment : Fragment() {

    lateinit var binding: FragmentPracticeQuestionsBinding

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
        setUpView()
    }

    private fun setUpView(){
        setUpEmptyView()
    }

    private fun setUpEmptyView(){
        binding.emptylayout.descriptionText.text = "No practice questions to resume"
        binding.emptylayout.textActionWord.text = "Start Practicing"
    }
}