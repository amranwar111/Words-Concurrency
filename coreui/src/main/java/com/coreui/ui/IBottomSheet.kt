package com.coreui.ui

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


interface IBottomSheet {
    fun setBottomSheetDialog(dialog: BottomSheetDialogFragment)
}

interface IBehavior<T>{
    fun setBehavior(t:T)
}