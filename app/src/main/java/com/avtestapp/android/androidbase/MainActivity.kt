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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.afollestad.materialdialogs.MaterialDialog
import com.avtestapp.android.androidbase.base.LoadingCallback
import com.avtestapp.android.androidbase.extensions.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_indicator.*

class MainActivity : AppCompatActivity(), LoadingCallback {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if(destination.id in arrayOf(
                    R.id.landingPageFragment
                )) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
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
}
