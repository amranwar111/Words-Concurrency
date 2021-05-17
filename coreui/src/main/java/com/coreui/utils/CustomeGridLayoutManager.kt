package com.coreui.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class CustomeGridLayoutManager( context: Context, spans:Int ) : GridLayoutManager(context,spans) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }


}