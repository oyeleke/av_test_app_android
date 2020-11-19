package com.avtestapp.android.androidbase.av_test.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.databinding.FragmentMarkedQuestionBinding

class MarkedQuestionFragment : Fragment() {

    private lateinit var binding: FragmentMarkedQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarkedQuestionBinding.inflate(inflater)
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
        binding.emptylayout.descriptionText.text = "You’ve not marked any question"
        binding.emptylayout.textActionWord.text = "Get Started"
    }


}