package com.avtestapp.android.androidbase.av_test.onboarding.upload_image

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.R

class UploadImageFragment : Fragment() {

    companion object {
        fun newInstance() = UploadImageFragment()
    }

    private lateinit var viewModel: UploadImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_image_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UploadImageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}