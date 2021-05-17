package com.coreui.ui.customViews

import android.content.Context

import android.graphics.Color
import android.graphics.drawable.Drawable

import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.coreui.CoreApp
import com.coreui.R
import android.text.InputFilter
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout


open class MaterialEditText : LinearLayout {
    var label: TextView? = null
    var inputLayout: TextInputLayout? = null
    var image: ImageView? = null

    //margins
    var labelMarginTop: Float = 0f
    var labelMarginBottom: Float = 0f
    var labelMarginEnd: Float = 0f
    var labelMarginStart: Float = 0f

    //styling
    var labelColor: Int = 0
    var editTextColor: Int = 0
    var labelTextSize: Float = 0f
    var editTextSize: Float = 0f
    var inputType = EditorInfo.TYPE_TEXT_VARIATION_NORMAL
    var imeOption = 0
    var maxLength = 0

    //data
    var hint: String = ""
        set(value) {
            field = value
            inputLayout?.editText?.hint = value
        }
    var labelText: String = ""
        set(value) {
            field = value
            label?.text = value
        }


    open var text: String = ""
//        set(value) {
//            field = value
//            inputLayout?.editText?.setText(value)
//        }

    var icon: Drawable? = null
        set(value) {
            field = value
            image?.setImageDrawable(value)
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttrs(context, attrs, defStyleAttr)
        inflateViews()
        setUiViews()

        val isArabic = (context.applicationContext as CoreApp).language.isLanguageArabic
        val gravity = if (isArabic!!) {
            Gravity.RIGHT or Gravity.CENTER_VERTICAL
        } else {
            Gravity.LEFT or Gravity.CENTER_VERTICAL
        }
        label?.gravity = gravity
        inputLayout?.editText?.gravity = gravity
//        inputLayout?.helperText
        setData()
        setStyling()

    }

    open fun inflateViews() {
        if (this.orientation == LinearLayout.HORIZONTAL) {
            View.inflate(context, R.layout.horizontal_edit_text, this)
        } else {
            View.inflate(context, R.layout.vertical_edit_text, this)
        }
    }

    private fun setUiViews() {
        label = findViewById(R.id.label)
        inputLayout = findViewById(R.id.edittext)
        image = findViewById(R.id.icon)
    }

    fun setStyling() {
//        label?.setTextColor(labelColor)
//        label?.textSize = labelTextSize.toFloat()

//        inputLayout?.editText?.setTextColor(editTextColor)
//        inputLayout?.editText?.textSize = editTextSize
        inputLayout?.background = null
        inputLayout?.editText?.setHintTextColor(Color.LTGRAY)
        inputLayout?.editText?.background = null
        label?.setPadding(0,0,0,5)

//        label?.setPadding(labelMarginStart,labelMarginTop,labelMarginStart,labelMarginBottom)
        (label?.layoutParams as MarginLayoutParams).apply {
            this.marginEnd = labelMarginEnd.toInt()
            this.marginStart = labelMarginStart.toInt()
            this.bottomMargin = labelMarginBottom.toInt()
            this.topMargin = labelMarginTop.toInt()
        }
    }

    fun setData() {
        label?.text = labelText
//        inputLayout?.editText?.setText(text)
        image?.setImageDrawable(icon)
        if (inputType>0) {
            inputLayout?.editText?.inputType = inputType
        }
        inputLayout?.editText?.imeOptions = imeOption
        if (maxLength>0) {
            inputLayout?.editText?.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength)))
        }
        inputLayout?.editText?.hint = hint

    }

    private fun setAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText, defStyleAttr, 0)
        val defaultdimen = context.resources.getDimension(R.dimen._4sdp)

        labelMarginTop = a.getDimension(R.styleable.MaterialEditText_labelTopMargin, defaultdimen)
        labelMarginBottom = a.getDimension(R.styleable.MaterialEditText_labelBottomMargin, defaultdimen)
        labelMarginEnd = a.getDimension(R.styleable.MaterialEditText_labelEndMargin, defaultdimen)
        labelMarginStart = a.getDimension(R.styleable.MaterialEditText_labelStartMargin, defaultdimen)


        //Styling
        val color = ContextCompat.getColor(context, android.R.color.darker_gray)
        val dimen = context.resources.getDimension(R.dimen._10ssp)
        labelColor = a.getColor(R.styleable.MaterialEditText_labelColor, color)
        editTextColor = a.getColor(R.styleable.MaterialEditText_edittextColor, color)
        labelTextSize = a.getDimension(R.styleable.MaterialEditText_labelTextSize, dimen)
        editTextSize = a.getDimension(R.styleable.MaterialEditText_edittextSize, dimen)
        inputType = a.getInt(R.styleable.MaterialEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
        imeOption = a.getInt(R.styleable.MaterialEditText_android_imeOptions, 0)
        maxLength = a.getInt(R.styleable.MaterialEditText_android_maxLength, 0)

        // data
        labelText = a.getString(R.styleable.MaterialEditText_label) ?: ""
        hint = a.getString(R.styleable.MaterialEditText_hint) ?: ""
        text = a.getString(R.styleable.MaterialEditText_text) ?: ""
        icon = a.getDrawable(R.styleable.MaterialEditText_icon)


    }

    companion object {
        @BindingAdapter("text")
        @JvmStatic
        fun setText(view: MaterialEditText, newValue: String?) {
            // Important to break potential infinite loops.
            if (view.inputLayout?.editText?.text?.toString() != newValue) {
//                view.text = newValue ?: ""
                view.inputLayout?.editText?.setText(newValue?:"")
            }
        }

        @InverseBindingAdapter(attribute = "text")
        @JvmStatic
        fun gettext(view: MaterialEditText): String? {
            return view.inputLayout?.editText?.text?.toString()
        }

        @BindingAdapter("textAttrChanged")
        @JvmStatic
        fun setListeners(
            view: MaterialEditText,
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {
                view.inputLayout?.editText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        attrChange.onChange()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }


}

