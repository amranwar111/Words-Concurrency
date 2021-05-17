package com.coreui.utils

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayout

fun TabLayout.changeTabsFont() {
    val vg = getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    for (j in 0 until tabsCount) {
        val vgTab = vg.getChildAt(j) as ViewGroup
        val tabChildsCount = vgTab.childCount
        for (i in 0 until tabChildsCount) {
            val tabViewChild = vgTab.getChildAt(i)
            if (tabViewChild is TextView) {
                tabViewChild.typeface = Typeface.createFromAsset(context.assets, "font/fontr.ttf")
            }
        }
    }
}