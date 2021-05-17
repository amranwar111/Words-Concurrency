package com.coreui.utils


import android.os.Bundle
import com.coreui.R
import com.coreui.ui.fragment.ToastFragmentNav


class GlobalToastNav {
    fun toastWarn(error: String?, type: String?): Bundle = Bundle().apply {
        putInt(ToastFragmentNav.IMAGE, R.drawable.ic_warning)
        putString(ToastFragmentNav.TEXT, error)
        putString(ToastFragmentNav.TYPE, type)

    }


    fun toastInfo(error: String?, type: String?): Bundle = Bundle().apply {
        putInt(ToastFragmentNav.IMAGE, R.drawable.ic_info)
        putString(ToastFragmentNav.TEXT, error)
        putString(ToastFragmentNav.TYPE, type)

    }


    fun toastSuccess(message: String?, type: String?): Bundle = Bundle().apply {
        putInt(ToastFragmentNav.IMAGE, R.drawable.ic_successic)
        putString(ToastFragmentNav.TEXT, message)
        putString(ToastFragmentNav.TYPE, type)

    }


}
