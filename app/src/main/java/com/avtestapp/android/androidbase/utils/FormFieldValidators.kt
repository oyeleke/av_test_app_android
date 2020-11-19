package com.avtestapp.android.androidbase.utils

import android.util.Patterns
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse

object FormFieldValidators {

    fun Any?.validateNonNullObject(): FormFieldValidationResult {
        (this != null).let {
            return FormFieldValidationResult(ok = it, error = if (it) null else "Should not be empty")
        }
    }

    fun String?.validateNotEmpty(): FormFieldValidationResult {
        (this?.isNotEmpty()).let {
            (it == null || it == false).let {
                return FormFieldValidationResult(ok = !it, error = if (!it) null else "Should not be empty")
            }
        }
    }

    fun String?.validatePasswordsMatch(password1: String): FormFieldValidationResult {
        (password1.isEmpty() || this.equals(password1)).let {
            return FormFieldValidationResult(ok = it, error = if (it) null else "passwords do not match")
        }
    }

    fun String?.validateEmailAddress(): FormFieldValidationResult {
        (this != null && Patterns.EMAIL_ADDRESS.matcher(this).matches()).let {
            return FormFieldValidationResult(ok = it, error = if (it) null else "Invalid email")
        }
    }

    fun ProfessionItemsResponse?.validateProfessionalItem(): FormFieldValidationResult {
        (this != null).let {
            return FormFieldValidationResult(ok = it, error = if (it) null else "Gender not selected")
        }
    }
}