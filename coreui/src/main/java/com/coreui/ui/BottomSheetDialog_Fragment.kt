package com.coreui.ui

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.coreui.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val FRAGMENTNAME = "FRAGMENTNAME"

class BottomSheetDialog_Fragment : BottomSheetDialogFragment() {

    companion object {
        fun open(className: String): BottomSheetDialog_Fragment {
            val fragment = BottomSheetDialog_Fragment()
            val bundle = Bundle()
            bundle.putString(FRAGMENTNAME, className)
            fragment.arguments = bundle
            return fragment
        }

    }


    fun showFragment(fragmentManager: FragmentManager) {
        this.show(fragmentManager, this.arguments?.getString(FRAGMENTNAME))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val container = FrameLayout(context!!)
        container.id = R.id.container
        return container
    }


    fun getFragmentClassName(): String? {
        return arguments?.getString(FRAGMENTNAME, null)
    }


    fun getFragment(): Class<Fragment>? {
        val clazz: Class<*>?
        try {
            clazz = Class.forName(getFragmentClassName()!!)

            return clazz as Class<Fragment>
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    lateinit var currentFragment: Fragment
    var behavior: IBehavior<*>? = null
    var currentBehavior: Any? = null
    fun <T> setBehavior(listener: T): BottomSheetDialog_Fragment {
        currentBehavior = listener
        return this
    }


    fun openFragment() {
        val fragmentClass = getFragment() ?: return
        currentFragment = fragmentClass.newInstance() ?: return

        currentFragment.arguments = arguments?.getBundle(LISTBUNDLE)
        currentFragment.arguments = arguments
        if (currentFragment is IBottomSheet) {
            (this.currentFragment as IBottomSheet).setBottomSheetDialog(this)
        }
        if (currentFragment is IBehavior<*>) {
            (this.currentFragment as IBehavior<Any?>).setBehavior(currentBehavior)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.container, currentFragment).commit()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openFragment()
    }
}