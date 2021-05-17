package com.coreui.ui.customViews

import android.content.Context

import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.coreui.R
import com.coreui.utils.setImage
import kotlinx.android.synthetic.main.vertical_edit_text_phone.view.*

class MaterialEditTextPhone : MaterialEditText {
    var phoneText: TextView? = null
    var phoneFlag: ImageView? = null
    var phoneTextSize:Float = 0f
    var phone: String? = null
        set(value) {
            field = value
            phoneText?.text = value
        }
    var phoneImage: String? = null
        set(value) {
            field = value
            phoneFlag?.setImage(value)
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttributes(context, attrs, defStyleAttr)
    }

    private fun setAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditTextPhone, defStyleAttr, 0)
        val dimen = context.resources.getDimension(R.dimen._10ssp)
        phone = a.getString(R.styleable.MaterialEditTextPhone_phone)
        phoneImage = a.getString(R.styleable.MaterialEditTextPhone_flag)
        phoneTextSize = a.getDimension(R.styleable.MaterialEditTextPhone_phoneTextSize,dimen)
//        phoneText?.textSize = phoneTextSize
        phoneFlag?.setImage(phoneImage)
    }

    override fun inflateViews() {
        if (this.orientation == LinearLayout.HORIZONTAL) {
            View.inflate(context, R.layout.horizontal_edit_text_phone, this)
        } else {
            View.inflate(context, R.layout.vertical_edit_text_phone, this)
        }
        phoneText = findViewById(R.id.phoneText)
        phoneFlag = findViewById(R.id.flag)


    }

    companion object {
        @BindingAdapter("phoneCode")
        @JvmStatic
        fun setPhoneCode(view: MaterialEditTextPhone, newValue: String?) {
            // Important to break potential infinite loops.
            if (view.phone!= newValue) {
                val code = newValue?.replace("\\+".toRegex(),"")
                view.phone = "${(code?:"")}+"
            }
        }

        @InverseBindingAdapter(attribute = "phoneCode")
        @JvmStatic
        fun getPhoneCode(view: MaterialEditTextPhone): String? {
            return view.phoneText?.text?.toString()
        }

        @BindingAdapter("phoneCodeAttrChanged")
        @JvmStatic
        fun setPhoneCodeListeners(
            view: MaterialEditTextPhone,
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {
                view.phoneText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        attrChange.onChange()
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }


        /*************************/
        @BindingAdapter("flagImage")
        @JvmStatic
        fun setFlag(view: MaterialEditTextPhone, newValue: String?) {
            // Important to break potential infinite loops.
            if (view.phoneImage!= newValue) {
                view.phoneImage = newValue?:""
            }
        }

        @InverseBindingAdapter(attribute = "flagImage")
        @JvmStatic
        fun getFlag(view: MaterialEditTextPhone): String? {
            return view.phoneImage
        }

        @BindingAdapter("flagImageAttrChanged")
        @JvmStatic
        fun flagImageAttrChanged(
            view: MaterialEditTextPhone,
            attrChange: InverseBindingListener?
        ) {
            if (attrChange != null) {

            }
        }
    }
}