package com.avtestapp.android.androidbase.av_test.auth.splash

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseFragment

class SplashScreenFragment : BaseFragment() {

    private val SPLASH_TIME_OUT: Long = 3000


    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed(
            {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLandingPageFragment())
            }, SPLASH_TIME_OUT
        )
    }

}