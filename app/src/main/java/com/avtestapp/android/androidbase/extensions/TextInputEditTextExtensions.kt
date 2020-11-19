package com.avtestapp.android.androidbase.extensions

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.util.HashSet

 val TextInputEditText.textChangeListeners: HashSet<OnTextChangeListener>
     get() = HashSet<OnTextChangeListener>()



interface OnTextChangeListener {
    fun onTextChange(view: TextInputEditText, newText: CharSequence)
}

fun TextInputEditText.addListener(onTextChangeListener: OnTextChangeListener) {
    textChangeListeners.add(onTextChangeListener)
}

fun TextInputEditText.removeListener(onTextChangeListener: OnTextChangeListener) {
    textChangeListeners.remove(onTextChangeListener)
}

fun TextInputEditText.removeAllListeners() {
    textChangeListeners.clear()
}

fun TextInputEditText.addListenerToTextWatcher(){
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //
        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            for (textChangeListener in textChangeListeners) {

            }
        }

        override fun afterTextChanged(s: Editable) {
            //
        }
    })
}