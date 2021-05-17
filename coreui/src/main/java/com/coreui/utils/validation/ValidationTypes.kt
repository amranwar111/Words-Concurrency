package com.coreui.utils.validation

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.coreui.R
import com.coreui.ui.customViews.MaterialEditText
import com.coreui.utils.setBorder
import com.google.android.material.chip.ChipGroup
import g.trippl3dev.com.validation.data.ValidationTag
import g.trippl3dev.com.validation.domain.interfaces.CheckerWatcher
import g.trippl3dev.com.validation.domain.interfaces.Enums.PATTERN
import g.trippl3dev.com.validation.domain.interfaces.Enums.ValidationState
import g.trippl3dev.com.validation.domain.interfaces.ValidationChecker
import g.trippl3dev.com.validation.domain.interfaces.ValidationToastMethod


interface ValidationTypes {
    open class TextViewType : ValidationChecker<TextView>() {
        companion object {
            const val TYPE = 0x011234
        }

        override fun check(validationTag: ValidationTag?, view: TextView?): Int {
            val value = view?.text?.toString()?.trim()
            if (value?.isEmpty()!!) return ValidationState.EMPTYERROR
            return if (value.matches(validationTag?.pattern?.toRegex()!!)) ValidationState.VALID else ValidationState.VALIDATIONERROR
        }

//        override fun setWatching(v: TextView?, validationTag: ValidationTag?) {
//            super.setWatching(v, validationTag)
//            v?.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//                override fun afterTextChanged(s: Editable) {
//                    checking(validationTag, v)
//                }
//            })
//        }

//        var drawable: Drawable? = null
        override fun getToasMethod(): ValidationToastMethod {
            return ValidationToastMethod { view, error ->
//                drawable = view?.background
//                view.requestFocus()
//                view?.setBorder(Color.RED, 2)
//                view?.animate()?.scaleX(1.1f)?.scaleY(1.1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())?.start()
//                Handler().postDelayed({
//                    view?.background = drawable
//                }, 1500)
//                view?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())?.start()
//                GlobalToast().showToast(view as View, error)
            }
        }


//        override fun getWatcher(): CheckerWatcher<TextView> {
//            return object : CheckerWatcher<TextView> {
//                override fun onNotValid(error: String?, view: TextView?) {
//                    if (drawable == null)
//                    drawable = view?.background
////                    view?.requestFocus()
////                    view?.setBorder(Color.RED, 2)
////                    view?.animate()?.scaleX(1.1f)?.scaleY(1.1f)?.setDuration(1400)
////                        ?.setInterpolator(BounceInterpolator())?.start()
////                    Handler().postDelayed({
////                        view?.background = drawable
////                    }, 1500)
////                    view?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())
////                        ?.start()
////                    GlobalToast().showToast(view as View, error)
//                }
//
//                override fun onValid(view: TextView?) {
////                    view?.background = null
//                }
//
//            }
//        }
    }

    open class ImageType : ValidationChecker<ImageView>() {
        companion object {
            const val TYPE = 0x011123
        }

        var drawable: Drawable? = null
        override fun check(validationTag: ValidationTag?, view: ImageView?): Int {
            return if (validationTag?.isIgnore!!) ValidationState.VALID else ValidationState.EMPTYERROR
        }

//        override fun getToasMethod(): ValidationToastMethod {
//            return ValidationToastMethod { view, error ->
//                drawable = view?.background
//                view?.setBorder(Color.RED, 3)
//                view?.animate()?.scaleX(1.1f)?.scaleY(1.1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())
//                    ?.start()
//                Handler().postDelayed({
//                    view?.background = drawable
//                }, 1500)
//                view?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())
//                    ?.start()
//                GlobalToast().showToast(view as View, error)
//            }
//        }
       /* override fun getWatcher(): CheckerWatcher<ImageView> {
            return object : CheckerWatcher<ImageView> {
                override fun onNotValid(error: String?, view: ImageView?) {
                    if (drawable == null)
                        drawable = view?.background
//                    view?.requestFocus()
                    if (view is CircleImageView){
                        view.borderColor = Color.RED
                        view.borderWidth = 2
                    }else
                    view?.setBorder(Color.RED, 2)
//                    view?.animate()?.scaleX(1.1f)?.scaleY(1.1f)?.setDuration(1400)
//                        ?.setInterpolator(BounceInterpolator())?.start()
//                    Handler().postDelayed({
//                        view?.background = drawable
//                    }, 1500)
//                    view?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(1400)?.setInterpolator(BounceInterpolator())
//                        ?.start()
//                    GlobalToast().showToast(view as View, error)
                }

                override fun onValid(view: ImageView?) {
                    if (view is CircleImageView){
                        view.borderColor = Color.GRAY
                        view.borderWidth = 2
                    }else
                    view?.background = null
                }

            }
        }*/


    }


    open class MEditTextConfirm : MEditText() {
        companion object {
            const val TYPE = 0x55555
        }
        override fun check(validationTag: ValidationTag?, view: MaterialEditText?): Int {
            val value = view?.inputLayout?.editText?.text?.toString()?.trim()
            if (value?.isEmpty()!!) return ValidationState.EMPTYERROR
            return if (value.matches(validationTag?.pattern?.toRegex()!!) &&
                        value.matches(PATTERN.PASSWORD.toRegex())
                ) ValidationState.VALID else ValidationState.VALIDATIONERROR
        }


    }
    open class MEditText : ValidationChecker<MaterialEditText>() {
        companion object {
            const val TYPE = 0x12311420
        }

        override fun check(validationTag: ValidationTag?, view: MaterialEditText?): Int {
            val value = view?.inputLayout?.editText?.text?.toString()?.trim()
            if (value?.isEmpty()!!) return ValidationState.EMPTYERROR
            return if (value.matches(validationTag?.pattern?.toRegex()!!)) ValidationState.VALID else ValidationState.VALIDATIONERROR
        }

        override fun setWatching(v: MaterialEditText?, validationTag: ValidationTag?) {
            super.setWatching(v, validationTag)
            v?.inputLayout?.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    checking(validationTag, v)
                }
            })
        }

        override fun getWatcher(): CheckerWatcher<MaterialEditText> {
            return object : CheckerWatcher<MaterialEditText> {
                override fun onNotValid(error: String?, view: MaterialEditText?) {
                    val id = view?.context?.resources?.getIdentifier(
                        "uncheack", "drawable",
                        view.context?.packageName
                    )
                    view?.findViewById<ImageView>(R.id.checkIcon)?.setImageResource(id!!)
                }

                override fun onValid(view: MaterialEditText?) {
                    val id = view?.context?.resources?.getIdentifier(
                        "cheack", "drawable",
                        view?.context?.packageName
                    )
                    view?.findViewById<ImageView>(R.id.checkIcon)?.setImageResource(id!!)
                }

            }
        }

    }

    open class ChipGroupType : ValidationChecker<ChipGroup>() {
        companion object {
            const val TYPE = 0x011345
        }

        override fun check(validationTag: ValidationTag?, view: ChipGroup?): Int {
            return if (view?.childCount!! <= 0) ValidationState.EMPTYERROR
            else ValidationState.VALID
        }
    }

    open class RadioGroupType : ValidationChecker<RadioGroup>() {
        companion object {
            const val TYPE = 0x01134533
        }

        override fun check(validationTag: ValidationTag?, view: RadioGroup?): Int {
            return if (view?.checkedRadioButtonId == -1) ValidationState.EMPTYERROR
            else ValidationState.VALID
        }
    }
}