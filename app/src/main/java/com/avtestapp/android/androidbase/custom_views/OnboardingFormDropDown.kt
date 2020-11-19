package com.avtestapp.android.androidbase.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.utils.Listable
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.custom_dropdown.view.*

class OnboardingFormDropDown : FrameLayout {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var editDropdown: ClickToSelectEditText<Listable<*>>
    private lateinit var overachingLayout: LinearLayoutCompat

    private var listener: OnClickListener? = null

    var text: CharSequence
        get() = editDropdown.text.toString()
        set(charSequence) = editDropdown.setText(charSequence)

    var hint: String
        get() = textInputLayout.hint.toString()
        set(value) {
            textInputLayout.hint = value
        }

    var error: CharSequence?
        get() = textInputLayout.error
        set(value) {
            textInputLayout.error = value
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        View.inflate(context, R.layout.custom_dropdown, this)

        initComponents()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OnboardingFormDropDown)

        try {
            hint = typedArray.getText(R.styleable.OnboardingFormDropDown_android_hint)?.toString()
                ?: ""
        } finally {
            typedArray.recycle()
        }

    }

    private fun initComponents() {
        editDropdown = findViewById(R.id.edit_dropdown)
        textInputLayout = findViewById(R.id.text_input_layout)
        overachingLayout = findViewById(R.id.overachingLayout)
    }

    fun setOnItemSelectedListener(listener: ClickToSelectEditText.OnItemSelectedListener<Listable<*>>) {
        editDropdown.setOnItemSelectedListener(listener)
    }

    fun addOnItemSelectedListener(onItemSelectedListener: ClickToSelectEditText.OnItemSelectedListener<Listable<*>>) {
        editDropdown.addOnItemSelectedListener(onItemSelectedListener)
    }

    fun removeOnItemSelectedListener(onItemSelectedListener: ClickToSelectEditText.OnItemSelectedListener<Listable<*>>) {
        editDropdown.removeOnItemSelectedListener(onItemSelectedListener)
    }

    fun removeAllListeners() {
        editDropdown.removeAllListeners()
    }

    fun setItems(items: List<Listable<*>>) {
        editDropdown.setItems(items)
    }

    fun getCurrentSelection(): Any? {
        return editDropdown.selection?.obj
    }

    fun setSelection(item: Any) {
        editDropdown.setRawSelection(item)
    }

    fun setSelection(index: Int) {
        editDropdown.setSelectionIndex(index)
    }

    fun clearSelection() {
        editDropdown.clearSelection()
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
        overachingLayout.performClick()
    }

}
