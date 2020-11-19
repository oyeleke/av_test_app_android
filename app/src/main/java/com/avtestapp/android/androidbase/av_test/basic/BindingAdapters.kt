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
package com.avtestapp.android.androidbase.av_test.basic

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avtestapp.android.androidbase.custom_views.ClickToSelectEditText
import com.avtestapp.android.androidbase.custom_views.OnboardingFormDropDown
import com.avtestapp.android.androidbase.utils.FormField
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber



@BindingAdapter("rxtext")
fun bindTextWatcherUnsafe(view: TextInputEditText, formField: FormField<*>?) {
    if (formField == null)
        return


    formField.value?.let {
        if (view.text.toString() != it.toString())
            view.setText (it.toString())
    }

    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //
        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            formField.setValueUnsafe(s.toString())
        }

        override fun afterTextChanged(s: Editable) {
            //
        }
    })

}

@BindingAdapter("rxtext")
fun bindTextWatcherUnsafe(view: OnboardingFormDropDown, formField: FormField<*>?) {
    if (formField == null)
        return

    view.removeAllListeners()

    formField.value?.let {
        if (view.getCurrentSelection() != it)
            view.setSelection(it)
    }

    view.addOnItemSelectedListener(ClickToSelectEditText.OnItemSelectedListener { item, selectedIndex ->
        formField.setValueUnsafe(item?.obj)
    })
}


