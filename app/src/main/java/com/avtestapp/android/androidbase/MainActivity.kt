/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avtestapp.android.androidbase

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.afollestad.materialdialogs.MaterialDialog
import com.avtestapp.android.androidbase.av_test.core.marked_question.MarkedQuestionsViewModel
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionViewModel
import com.avtestapp.android.androidbase.base.BaseActivity
import com.avtestapp.android.androidbase.base.BaseBottomSheetDialogFragment
import com.avtestapp.android.androidbase.base.BaseFragment
import com.avtestapp.android.androidbase.base.LoadingCallback
import com.avtestapp.android.androidbase.extensions.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_indicator.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseActivity(), LoadingCallback, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var currentFragment: BaseFragment
    private lateinit var currentBottomSheetFragment: BaseBottomSheetDialogFragment
    private lateinit var viewModel: QuestionViewModel
    private lateinit var markedQuestionsViewModel: MarkedQuestionsViewModel


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        (this.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionViewModel::class.java)
        markedQuestionsViewModel = ViewModelProvider(this, viewModelFactory).get(MarkedQuestionsViewModel::class.java)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(
            bottom_navigation, navController
        )


        navController.addOnDestinationChangedListener{_, destination, _ ->
            if(destination.id in arrayOf(
                    R.id.splashScreenFragment,
                    R.id.landingPageFragment,
                    R.id.loginFragment,
                    R.id.signUpFragment,
                    R.id.forgotPasswordFragment,
                    R.id.verificationFragment,
                    R.id.resetPasswordFragment,
                    R.id.onboardingStartFragment,
                    R.id.uploadImageFragment,
                    R.id.dashboardFragment,
                    R.id.onboarding3Fragment,
                    R.id.studyQuestionFragment,
                    R.id.practiceQuestionsFragment,
                    R.id.reviewQuestionsFragment,
                    R.id.markedQuestionsFragment
                )) {
                toolbar.hide()
            } else {
                toolbar.show()
            }

            if(destination.id in arrayOf(
                    R.id.splashScreenFragment,
                    R.id.landingPageFragment,
                    R.id.loginFragment,
                    R.id.signUpFragment,
                    R.id.forgotPasswordFragment,
                    R.id.verificationFragment,
                    R.id.resetPasswordFragment,
                    R.id.onboardingStartFragment,
                    R.id.uploadImageFragment,
                    R.id.onboarding3Fragment
                )) {
                bottom_navigation.hide()
            } else {
                bottom_navigation.show()
            }
        }
    }


    fun setUpToolBar(toolbarTitle: String, isRootPage: Boolean = false) {
        supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(!isRootPage)
            setHomeAsUpIndicator(if (!isRootPage) R.drawable.ic_arrow_back_white_24dp else 0)
            toolbar.title = toolbarTitle
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun showLoading() {
        showLoading(R.string.default_loading_message)
    }

    override fun showLoading(resId: Int) {
        showLoading(getString(resId))
    }

    override fun showLoading(message: String) {
        hideKeyBoard()
        progressMessage.text = message
        loading_layout_container.showViewWithChildren()
        disableTouch()
    }

    override fun dismissLoading() {
        loading_layout_container.hide()
        enableTouch()
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }

    override fun showError(message: String) {
        hideKeyBoard()
        dismissLoading()
        MaterialDialog(this).show {
            title(text = message)
            positiveButton(R.string.ok)
        }
    }

    fun setCurrentFragment(baseFragment: BaseFragment) {
        currentFragment = baseFragment
    }

    fun setCurrentBottomSheetFragment(baseBottomSheetDialogFragment: BaseBottomSheetDialogFragment){
        currentBottomSheetFragment = baseBottomSheetDialogFragment
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.dashboardFragment -> {
                navController.navigate(R.id.dashboardFragment)
            }
            R.id.questionTypeFragment -> {
                Timber.e("I am clicked")
                navController.navigate(R.id.questionTypeFragment)
            }

            R.id.settingsFragment -> {
                showToast("Testing")
                navController.navigate(R.id.settingsFragment)
            }
        }
        return true
    }

    companion object{


    }

}
