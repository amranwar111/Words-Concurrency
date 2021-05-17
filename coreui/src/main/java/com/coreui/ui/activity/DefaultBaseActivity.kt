package com.coreui.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import com.blankj.utilcode.util.ActivityUtils
import com.coredata.module.LanguageModule
import com.coreui.CoreApp
import com.coreui.R
import com.coreui.utils.IValidation
import dagger.android.support.DaggerAppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class DefaultBaseActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()
    lateinit var myApplication: CoreApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setBackgroundDrawableResource(R.drawable.bg)
        myApplication.language.isEnglish.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                myApplication.language.setLocale(this@DefaultBaseActivity,   myApplication.language.getLocality()!!)
                recreate()
            }
        })
    }

        override fun attachBaseContext(newBase: Context?) {
//        AndroidInjection.inject(this)
        myApplication = newBase!!.applicationContext as CoreApp
        super.attachBaseContext(ViewPumpContextWrapper.wrap(myApplication.language.attach(newBase)))

    }


    override fun onResume() {
        super.onResume()
    }



    fun checkValidation(): Boolean {
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            if (it is IValidation) {
                if (!it.isValid()) {
                    return false
                }
            }
        }
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            if (w == null) return false
            w.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]
            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right
                        || y < w.top || y > w.bottom)
            ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
            }
        }
        return ret
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(
            myApplication.language.applyOverrideConfiguration(
                baseContext,
                overrideConfiguration
            )
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_YES -> {
                ActivityUtils.finishAllActivities()
                val intent = baseContext.packageManager.getLaunchIntentForPackage(
                    baseContext.packageName
                )
                intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)


            } // Night mode is not active, we're using the light theme
//            Configuration.UI_MODE_NIGHT_YES -> {} // Night mode is active, we're using dark theme
        }

    }


}
