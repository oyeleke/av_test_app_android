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
package com.avtestapp.android.androidbase.extensions

import android.graphics.Point
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ScrollView
import androidx.core.view.children
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.avtestapp.android.androidbase.custom_views.OnboardingFormDropDown
import com.avtestapp.android.androidbase.utils.FormField
import com.google.android.material.internal.ViewUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = GONE
}

fun ViewGroup.showViewWithChildren() {
    show()
    for (view in children) {
        view.show()
    }

}

fun scrollToView(
    scrollViewParent: ScrollView,
    view: View
) {
    // Get deepChild Offset
    val childOffset = Point()
    getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
    // Scroll to child.
    scrollViewParent.smoothScrollTo(0, childOffset.y)
}

private fun getDeepChildOffset(
    mainParent: ViewGroup,
    parent: ViewParent,
    child: View,
    accumulatedOffset: Point
) {
    val parentGroup: ViewGroup = parent as ViewGroup
    accumulatedOffset.x += child.left
    accumulatedOffset.y += child.top
    if (parentGroup.equals(mainParent)) {
        return
    }
    getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset)
}

fun TextInputEditText.setFeedbackSource(context: LifecycleOwner, formField: FormField<*>, scrollableParent: ScrollView? = null) {
    formField.error.observe(context, Observer {
        this.error = it
    })
    formField.requestFocus.observe(context, Observer {
        scrollableParent?.let { sp -> scrollToView(sp, this) }
    })
}

fun OnboardingFormDropDown.setFeedbackSource(context: LifecycleOwner, formField: FormField<*>, scrollableParent: ScrollView? = null) {
    formField.error.observe(context, Observer {
        this.error = it
    })
    formField.requestFocus.observe(context, Observer {
        scrollableParent?.let { sp -> scrollToView(sp, this) }
    })
}
