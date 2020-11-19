package com.avtestapp.android.androidbase.base

import com.avtestapp.android.androidbase.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(){


    protected val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    override fun onStart() {
        super.onStart()
        mainActivity.setCurrentBottomSheetFragment(this)
    }

}