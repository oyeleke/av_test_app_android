package com.avtestapp.android.androidbase.utils

data class FormFieldValidationResult(
    val ok: Boolean,
    val feedback: String? = null, // Should this be a list?
    val error: String? = null
)