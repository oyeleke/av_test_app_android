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
package com.avtestapp.android.androidbase.base

import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.avtestapp.android.androidbase.MainActivity
import com.avtestapp.android.androidbase.R
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    protected val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    override fun onStart() {
        super.onStart()
        mainActivity.setCurrentFragment(this)
    }

    fun showDialogWithAction(
        title: String? = null,
        body: String? = null,
        @StringRes positiveRes: Int = R.string.ok,
        positiveAction: (() -> Unit)? = null,
        @StringRes negativeRes: Int? = null,
        negativeAction: (() -> Unit)? = null,
        cancelOnTouchOutside: Boolean = true,
        autoDismiss: Boolean = true
    ) = mainActivity.showDialogWithAction(
        title,
        body,
        positiveRes,
        positiveAction,
        negativeRes,
        negativeAction,
        cancelOnTouchOutside,
        autoDismiss
    )

    protected fun showSnackBar(@StringRes stringRes: Int) =
        mainActivity.showSnackBar(getString(stringRes))

    protected fun showSnackBar(message: String) = mainActivity.showSnackBar(message)

    protected fun showToast(message: String) = mainActivity.showToast(message)

    protected fun showToast(@StringRes stringRes: Int) =
        mainActivity.showToast(getString(stringRes))


    // Return true if you handle the back press in your fragment
    open fun onBackPressed(): Boolean = false

    // Override if more than this basic setup is needed
}
