package com.avtestapp.android.androidbase.av_test.auth.splash

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseFragment
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import javax.inject.Inject

class SplashScreenFragment : BaseFragment() {

    private val SPLASH_TIME_OUT: Long = 3000

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed(
            {
                if (sharedPrefs.getIfUserHasLoggedInBefore()){
                    findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
                }else {
                    findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLandingPageFragment())
                }
            }, SPLASH_TIME_OUT
        )
    }

}