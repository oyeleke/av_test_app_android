package com.avtestapp.android.androidbase.av_test.onboarding

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.R

class Onboarding3Fragment : Fragment() {

    companion object {
        fun newInstance() = Onboarding3Fragment()
    }

    private lateinit var viewModel: Onboarding3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.onboarding3_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Onboarding3ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}