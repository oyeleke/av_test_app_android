package com.avtestapp.android.androidbase.av_test.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.databinding.FragmentStudyQuestionBinding

class StudyQuestionFragment : Fragment() {

    private lateinit var binding: FragmentStudyQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyQuestionBinding.inflate(inflater)
        binding.lifecycleOwner
        return  binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        setUpEmptyView()
    }

    private fun setUpEmptyView(){
        binding.emptylayout.descriptionText.text = "No study question to resume"
        binding.emptylayout.textActionWord.text = "Start Studying"
    }

}