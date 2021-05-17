package com.coreui.ui.fragment.list

import com.coreui.presentation.base.BaseIntent

open  class BaseListIntent :BaseIntent{
    class ListBaseIntent(val page:Int): BaseListIntent()
}