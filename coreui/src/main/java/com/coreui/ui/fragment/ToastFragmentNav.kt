package com.coreui.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.coreui.R
import com.coreui.ui.AnimatorListenr
import com.coreui.ui.DialogeFragment
import com.coreui.ui.IDialog
import com.coreui.utils.setBackgroundDrawableColor
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class ToastFragmentNav : DefaultBaseDialogFragment() {


    companion object {
        val IMAGE = "IMAGE"
        val TYPE = "TYPE"
        val TEXT = "TEXT"
        val COLOR = "COLOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var image: ImageView? = null

    private var text: TextView? = null
    private var view_container: View? = null
    //    full view
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.toast_dialoge_layout, container, false)

        image = view.findViewById(R.id.titleImage)
        text = view.findViewById(R.id.titleDesc)
        view_container = view.findViewById<View>(R.id.view_container)
        val hide = view.findViewById<TextView>(R.id.request)
        hide?.setBackgroundDrawableColor(ContextCompat.getColor(context!!, R.color.blue))
        hide.setOnClickListener {
            dismiss()
            getType()?.let { EventBus.getDefault().post(it) }

        }
        view_container?.setOnClickListener { dismiss() }
        image?.setImageResource(getImage())
        text?.text = getText()

        return view
    }

    private fun getImage(): Int {
        return arguments?.getBundle("bundle")!!.getInt(IMAGE, R.drawable.close_1)
    }

    private fun getText(): String {
        return arguments?.getBundle("bundle")!!.getString(TEXT)!!
    }

    private fun getType(): String?{
        return arguments?.getBundle("bundle")!!.getString(TYPE, null)
    }

    private fun getColor(): Int {
        return arguments?.getInt(COLOR)!!
    }
}