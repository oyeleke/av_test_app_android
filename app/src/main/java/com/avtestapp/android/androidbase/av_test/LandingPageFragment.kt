package com.avtestapp.android.androidbase.av_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseFragment
import com.avtestapp.android.androidbase.databinding.FragmentLandingPageBinding

class LandingPageFragment : BaseFragment() {

    lateinit var binding: FragmentLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingPageBinding.inflate(inflater)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar(getString(R.string.cat_breeds), true)

        binding.createAccountButton.setOnClickListener {
            findNavController().navigate(LandingPageFragmentDirections.actionLandingPageFragmentToSignUpFragment())
        }

        binding.signInButton.setOnClickListener {
            findNavController().navigate(LandingPageFragmentDirections.actionLandingPageFragmentToLoginFragment())
        }
    }
}
