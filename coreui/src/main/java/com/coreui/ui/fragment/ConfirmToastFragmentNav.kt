package com.coreui.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.coreui.R
import com.coreui.ui.*
import com.coreui.utils.setBackgroundDrawableColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toast_confirm_dialoge_layout.*
import org.greenrobot.eventbus.EventBus

class ConfirmToastFragmentNav : DefaultBaseDialogFragment() {


    companion object {
        val IMAGE = "IMAGE"
        val TEXT = "TEXT"
        val TYPE = "TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var image: ImageView? = null

    private var text: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.toast_confirm_dialoge_layout, container, false)
        image = view.findViewById(R.id.titleImage)
        text = view.findViewById(R.id.titleDesc)
        image?.setImageResource(getImage())
        text?.text = getText()

        return view
    }

    private fun getImage(): Int {
        return arguments?.getBundle("bundle")!!.getInt(IMAGE, R.drawable.close_1)
    }

    private fun getType(): String? {
        return arguments?.getBundle("bundle")!!.getString(TYPE, null)
    }

    private fun getText(): String {
        return arguments?.getBundle("bundle")!!.getString(TEXT)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request?.setBackgroundDrawableColor(ContextCompat.getColor(context!!, R.color.blue))
        request.setOnClickListener {
            dialog?.dismiss()
            getType()?.let { EventBus.getDefault().post(it+"Accept") }

        }
        cancel?.setOnClickListener {
            dialog?.dismiss()
            getType()?.let { EventBus.getDefault().post(it+"Cancel") }

        }
    }

}