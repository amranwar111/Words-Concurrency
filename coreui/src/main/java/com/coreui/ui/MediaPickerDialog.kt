package com.coreui.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coreui.R
import com.coreui.ui.fragment.DefaultBaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.media_bottom_sheet.*
@AndroidEntryPoint
class MediaPickerDialog : DefaultBaseFragment(),IBottomSheet ,IBehavior<MediaPicker>{
    override fun setBehavior(t: MediaPicker) {
        this.mediaPicker = t
    }



    private lateinit var dialog: BottomSheetDialogFragment
    private lateinit var mediaPicker: MediaPicker

    override fun setBottomSheetDialog(dialog: BottomSheetDialogFragment) {
        this.dialog = dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.media_bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera.setOnClickListener {
            mediaPicker.openCamera()
            dialog?.dismiss()
        }
        filePicker.setOnClickListener {
            mediaPicker.openFile()
            dialog?.dismiss()
        }
        cancel.setOnClickListener {
             dialog.dismiss()
            dialog?.dismiss()
        }
    }

}
interface MediaPicker{
    fun openCamera()
    fun openFile()
}