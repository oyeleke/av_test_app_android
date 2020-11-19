package com.avtestapp.android.androidbase.utils

import androidx.lifecycle.MediatorLiveData

class Form {

    private val formFields = arrayListOf<FormField<*>>()

    /*
    * True when all the formfields' values are verified ok
    * Observe to activate a submit button, continue button, etc, if true
    * */
    val isComplete = MediatorLiveData<Boolean>()

    /*
    * Runs validators for all the formfields in this form, until one fails
    * requests focus for the failed field
    *
    * Observe [FormField#feedback] and if the feedback is [REQUEST_FOCUS_FEEDBACK]
    * scroll to the view attached to the formfield to bring it to focus
    * */
    fun verify(): Boolean {
        for (form in formFields) {
            if (!form.verify()) {
                form.requestFocus.value = null
                return false
            }
        }
        return true
    }

    private fun softVerify(): Boolean {
        return formFields.all { it.ok }
    }

    fun addFormField(formField: FormField<*>) {
        this.formFields.add(formField)

        isComplete.addSource(formField) {
            if (it != null) {
                formField.verify()
                isComplete.value = softVerify()
            }
        }
    }

    /*
    * Add multiple formfields at once
    * */
    fun addFormFields(formFields: List<FormField<*>>) {
        this.formFields.addAll(formFields)

        for (field in formFields) {
            isComplete.addSource(field) {
                if (it != null) {
                    field.verify()
                    isComplete.value = softVerify()
                }
            }
        }
    }

    /*
    * Add multiple formfields at once
    * */
    fun addFormFields(vararg formFields: FormField<*>) {
        this.formFields.addAll(formFields)

        for (field in formFields) {
            isComplete.addSource(field) {
                if (it == null) {
                    field.verify()
                    isComplete.value = softVerify()
                }
            }
        }
    }

    /*
    * Remove a formfield
    * */
    fun removeFormField(formField: FormField<*>) {
        this.formFields.remove(formField)
        isComplete.removeSource(formField)
    }

}