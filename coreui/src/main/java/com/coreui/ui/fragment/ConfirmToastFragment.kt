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

class ConfirmToastFragment : DefaultBaseFragment(), IDialog,IListListenerSetter {
    private lateinit var dialogListener: IListItemSelected<Boolean>

    override fun setListener(listener: IListItemSelected<*>) {
        this.dialogListener = listener as IListItemSelected<Boolean>
    }

    private var dialog: DialogFragment? = null

    override fun setDialogFragment(dialog: DialogFragment) {
        this.dialog = dialog
        (this.dialog as DialogeFragment)
            .animator = object : AnimatorListenr {
            override fun animate() {
                val decorView = dialog.dialog?.window?.decorView
                decorView?.animate()?.rotation(360f)
                    ?.alpha(0f)
                    ?.scaleX(0f)
                    ?.scaleY(0f)
//                ?.setStartDelay(300)
                    ?.setDuration(300)
                    ?.start()
            }

            override fun getAnimationTime(): Long {
                return 300L
            }

        }


    }

    companion object {
        val IMAGE = "IMAGE"
        val TEXT = "TEXT"
        val COLOR = "COLOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var image: ImageView? = null

    private var text: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.toast_confirm_dialoge_layout, container, false)
        image = view.findViewById(R.id.titleImage)
        text = view.findViewById(R.id.titleDesc)
//        var hide = view.findViewById<TextView>(R.id.request)

        image?.setImageResource(getImage())
        text?.text = getText()

        return view
    }

    fun getImage(): Int {
        return arguments?.getInt(IMAGE, R.drawable.close_1)!!
    }

    override fun onResume() {
        super.onResume()
        dialog?.dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }
    fun getText(): String {
        return arguments?.getString(TEXT)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request?.setBackgroundDrawableColor(ContextCompat.getColor(context!!, R.color.blue))
        request.setOnClickListener {
            dialog?.dismiss()
            dialogListener.onItemSelected(true)
//            (dialog as DialogeFragment).dis

        }
        cancel?.setOnClickListener {
            dialogListener.onItemSelected(false)
            dialog?.dismiss()
        }
    }
    fun getColor(): Int {
        return arguments?.getInt(COLOR)!!
    }
}