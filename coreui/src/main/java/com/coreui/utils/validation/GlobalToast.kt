package com.coreui.utils.validation

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.StringUtils
import com.coreui.R
import com.coreui.ui.DialogeFragment
import com.coreui.ui.FRAGMENTNAME
import com.coreui.ui.IListItemSelected
import com.coreui.ui.IListListenerSetter
import com.coreui.ui.fragment.ConfirmToastFragment
import com.coreui.ui.fragment.ToastFragment
import com.coreui.utils.getActivity
import g.trippl3dev.com.validation.domain.interfaces.ValidationToastMethod
import io.reactivex.functions.Consumer
import com.coreui.ui.fragment.BaseFragment
import java.lang.Exception


//import io.reactivex.functions.Consumer
const val time: Long = 1500L

class GlobalToast : ValidationToastMethod {
    override fun showToast(view: View?, error: String?) {
//        Toast.makeText(view?.context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error))
            showDialogWith(R.drawable.ic_warning, error!!, R.color.warn, view?.context!!, null)
    }

    fun showToast(view: View?, error: String?, consumer: Consumer<Boolean>) {
//        Toast.makeText(view?.context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error))
            showDialogWith(R.drawable.ic_warning, error!!, R.color.warn, view?.context!!, consumer)
    }


    fun showToastWarn(context: Context?, error: String?, consumer: Consumer<Boolean>) {
//        Toast.makeText(view?.context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error))
            showDialogWith(R.drawable.ic_warning, error!!, R.color.warn, context!!, consumer)
    }

    fun showConfirm(context: Context?, error: String?, consumer: Consumer<Boolean>) {
        if (!StringUtils.isEmpty(error))
            showConfirmDialogWith(R.drawable.ic_info, error!!, R.color.warn, context!!, consumer)
    }

    fun showConfirm(
        context: Context?,
        error: String?,
        consumer: Consumer<Boolean>,
        consumer2: Consumer<Boolean>
    ) {
        if (!StringUtils.isEmpty(error))
            showConfirmDialogWith(
                R.drawable.ic_info,
                error!!,
                R.color.warn,
                context!!,
                consumer,
                consumer2
            )
    }


    fun showConfirmApi(
        context: Context?,
        error: String?,
        consumer: Consumer<Boolean>,
        consumer2: Consumer<Boolean>
    ) {
        if (!StringUtils.isEmpty(error))
            showConfirmDialogWith2(
                R.drawable.ic_info,
                error!!,
                R.color.warn,
                context!!,
                consumer,
                consumer2
            )
    }

    fun showToastWarn(context: Context?, error: String?) {
//        Toast.makeText(view?.context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error) && !error!!.contains("JSON") && !error.contains("null") && !error.contains(
                "lost"
            ) && !error.contains(
                "out"
            ) && !error.contains("DOCTYPE") && !error.contains("html") && !error.contains("error") && !error.contains(
                "peer"
            )
        ) {
            showDialogWith(R.drawable.ic_warning, error, R.color.warn, context!!, null)
        } else {
            showDialogWith(
                R.drawable.ic_warning,
                "حدث خطأ في الإتصال \n  برجاء المحاوله مره اخرى",
                R.color.warn,
                context!!,
                null
            )

        }
    }

    fun showToastInfo(context: Context?, error: String?) {
//        Toast.makeText(view?.context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error))
            showDialogWith(R.drawable.ic_info, error!!, R.color.warn, context!!, null)
    }

    fun showToast(context: Context, error: String?) {
//        Toast.makeText(context, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error) && !error!!.contains("JSON") && !error.contains("null") && !error.contains(
                "lost"
            ) && !error.contains(
                "out"
            ) && !error.contains("DOCTYPE") && !error.contains("html") && !error.contains("error") && !error.contains(
                "peer"
            )
        ) {
            showDialogWith(R.drawable.ic_warning, error, R.color.warn, context!!, null)
        } else {
            showDialogWith(
                R.drawable.ic_warning,
                "حدث خطأ في الإتصال \n  برجاء المحاوله مره اخرى",
                R.color.warn,
                context!!,
                null
            )

        }
    }

    fun showToast(context: Context, error: String?, consumer: Consumer<Boolean>?) {
//        Toast.makeText(context!!, error+"", Toast.LENGTH_LONG).show()
        if (!StringUtils.isEmpty(error) && !error!!.contains("JSON") && !error.contains("null") && !error.contains(
                "lost"
            ) && !error.contains(
                "out"
            ) && !error.contains("DOCTYPE") && !error.contains("html") && !error.contains("error") && !error.contains(
                "peer"
            )
        ) {
            showDialogWith(R.drawable.ic_warning, error, R.color.warn, context!!, consumer)
        } else {
            showDialogWith(
                R.drawable.ic_warning,
                "حدث خطأ في الإتصال \n  برجاء المحاوله مره اخرى",
                R.color.warn,
                context!!,
                null
            )

        }
    }

    fun showSuccessToast(context: Context, message: String?, consumer: Consumer<Boolean>?) {
//        Toast.makeText(context!!, message+"", Toast.LENGTH_LONG).show()
        val successColor = ContextCompat.getColor(context, R.color.success)
        if (!StringUtils.isEmpty(message)) {
            val bundle = Bundle().apply {
                putInt(ToastFragment.IMAGE, R.drawable.ic_successic)
                putString(ToastFragment.TEXT, message!!)
                putInt(ToastFragment.COLOR, successColor)
            }
            val dialog = DialogeFragment.openDilogFragment(
                context!!.getActivity()?.supportFragmentManager!!,
                ToastFragment::class.java.name,
                bundle
            )
            dialog.setDismissListner(object : DialogInterface {
                override fun dismiss() {
                    consumer?.accept(true)
                }

                override fun cancel() {
                    consumer?.accept(true)
                }

            })
            Handler().postDelayed({
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                }

            }, time)
        }
    }

    //    twitterkit-pAXi113lNKbiFGmMicdb0KwXQ://
    fun showSuccessToast(view: View?, message: String?, consumer: Consumer<Boolean>?) {
        if (!StringUtils.isEmpty(message)) {
            val successColor = ContextCompat.getColor(view?.context!!, R.color.success)
            val bundle = Bundle().apply {
                putInt(ToastFragment.IMAGE, R.drawable.ic_successic)
                putString(ToastFragment.TEXT, message!!)
                putInt(ToastFragment.COLOR, successColor)
            }
            val dialog = DialogeFragment.openDilogFragment(
                view?.context!!.getActivity()?.supportFragmentManager!!,
                ToastFragment::class.java.name,
                bundle
            )
            dialog.setDismissListner(object : DialogInterface {
                override fun dismiss() {
                    consumer?.accept(true)
                }

                override fun cancel() {
                    consumer?.accept(true)
                }

            })
            Handler().postDelayed({
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                }

            }, time)
        }
    }


    fun showDialogSuccessToast(
        view: View?,
        message: String?,
        consumer: Consumer<Boolean>?
    ): DialogeFragment? {
        if (!StringUtils.isEmpty(message)) {
            val successColor = ContextCompat.getColor(view?.context!!, R.color.success)
            val bundle = Bundle().apply {
                putInt(ToastFragment.IMAGE, R.drawable.ic_successic)
                putString(ToastFragment.TEXT, message!!)
                putInt(ToastFragment.COLOR, successColor)
            }
            val dialog = DialogeFragment.openDilogFragment(
                view?.context!!.getActivity()?.supportFragmentManager!!,
                ToastFragment::class.java.name,
                bundle
            )
            dialog.setDismissListner(object : DialogInterface {
                override fun dismiss() {
                    consumer?.accept(true)
                }

                override fun cancel() {
                    consumer?.accept(true)
                }

            })
//            Handler().postDelayed({
//                try {
//                    dialog.dismiss()
//                } catch (e: Exception) {
//                }
//
//            }, time)

            return dialog
        }
        return null

    }

    fun showDialogWith(
        image: Int,
        text: String,
        color: Int,
        context: Context,
        consumer: Consumer<Boolean>?
    ) {
        val colorRes = ContextCompat.getColor(context, color)
        val bundle = Bundle().apply {
            putInt(ToastFragment.IMAGE, image)
            putString(ToastFragment.TEXT, text)
            putInt(ToastFragment.COLOR, colorRes)
        }
        try {


            val dialog = DialogeFragment.openDilogFragment(
                context.getActivity()?.supportFragmentManager!!,
                ToastFragment::class.java.name,
                bundle
            )
            dialog.setDismissListner(object : DialogInterface {
                override fun dismiss() {
                    consumer?.accept(true)
                }

                override fun cancel() {
                    consumer?.accept(true)

                }

            })
        } catch (e: Exception) {

        }
    }


}

fun showConfirmDialogWith(
    image: Int,
    text: String,
    color: Int,
    context: Context,
    consumer: Consumer<Boolean>?
) {
    val colorRes = ContextCompat.getColor(context, color)
    val bundle = Bundle().apply {
        putInt(ConfirmToastFragment.IMAGE, image)
        putString(ConfirmToastFragment.TEXT, text)
        putInt(ConfirmToastFragment.COLOR, colorRes)
    }
    val dialog = DialogeFragment.openDilogFragment(
        context.getActivity()?.supportFragmentManager!!,
        ConfirmToastFragment::class.java.name,
        bundle
    )
    dialog.setIListItemSelected(object : IListItemSelected<Boolean> {
        override fun onItemSelected(t: Boolean) {
            if (t) {
                consumer?.accept(t)
            }
        }

    })


}

fun showConfirmDialogWith(
    image: Int,
    text: String,
    color: Int,
    context: Context,
    consumer: Consumer<Boolean>?,
    consumer2: Consumer<Boolean>?
) {
    val colorRes = ContextCompat.getColor(context, color)
    val bundle = Bundle().apply {
        putInt(ConfirmToastFragment.IMAGE, image)
        putString(ConfirmToastFragment.TEXT, text)
        putInt(ConfirmToastFragment.COLOR, colorRes)
    }
    val dialog = DialogeFragment.openDilogFragment(
        context.getActivity()?.supportFragmentManager!!,
        ConfirmToastFragment::class.java.name,
        bundle
    )
    dialog.setIListItemSelected(object : IListItemSelected<Boolean> {
        override fun onItemSelected(t: Boolean) {
            if (t) {
                consumer?.accept(t)
            }
        }

    })

    dialog.setDismissListner(object : DialogInterface {
        override fun dismiss() {
            consumer2?.accept(true)
        }

        override fun cancel() {
            consumer2?.accept(true)
        }
    })


}

fun showConfirmDialogWith2(
    image: Int,
    text: String,
    color: Int,
    context: Context,
    consumer: Consumer<Boolean>?,
    consumer2: Consumer<Boolean>?
) {
    val colorRes = ContextCompat.getColor(context, color)
    val bundle = Bundle().apply {
        putInt(ConfirmToastFragment.IMAGE, image)
        putString(ConfirmToastFragment.TEXT, text)
        putInt(ConfirmToastFragment.COLOR, colorRes)
    }
    val dialog = DialogeFragment.openDilogFragment(
        context.getActivity()?.supportFragmentManager!!,
        ConfirmToastFragment::class.java.name,
        bundle
    )
    dialog.setIListItemSelected(object : IListItemSelected<Boolean> {
        override fun onItemSelected(t: Boolean) {
            if (t) {
                consumer?.accept(t)
            } else {
                consumer2?.accept(true)
            }
        }

    })

}

fun getActiveFragment(context: Context): String? {
    if (context.getActivity()?.supportFragmentManager!!.getBackStackEntryCount() === 0) {
        return null
    }
    val tag =
        context.getActivity()?.supportFragmentManager!!.getBackStackEntryAt(context.getActivity()?.supportFragmentManager!!.getBackStackEntryCount() - 1)
            .getName()
    return context.getActivity()?.supportFragmentManager!!.findFragmentByTag(tag).toString()
}
