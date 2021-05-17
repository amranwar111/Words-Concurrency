package com.coreui.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.Property
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import androidx.fragment.app.FragmentActivity

/**
 * Created by ahmed.saad on 11/29/16.
 */

object AnimationUtils {
    fun propertyAnimation(v: View, item: Property<View, Float>, start: Float, value: Float): ObjectAnimator {
        val fadAnim = ObjectAnimator.ofFloat(v, item, start, value)
        fadAnim.duration = 3000
        fadAnim.repeatCount = ValueAnimator.INFINITE
        fadAnim.repeatMode = ValueAnimator.REVERSE
        fadAnim.interpolator = BounceInterpolator()
        return fadAnim
    }

    fun loadAnimationWithListner(activity: FragmentActivity?, v: View, animRes: Int, animationAction: AnimationAction) {
        v?.clearAnimation()
        var animator = getAnimation(activity!!.baseContext, animRes)
        animator?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }
            override fun onAnimationStart(p0: Animation?) {
                if (animationAction == AnimationAction.SHOW)
                    v.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animation?) {
                if (animationAction == AnimationAction.HIDE)
                    v.visibility = View.GONE
            }

        })
        v.startAnimation(animator)
    }

    private fun getAnimation(activity: Context, animRes: Int): Animation? {
        return AnimationUtils.loadAnimation(activity, animRes)
    }

    enum class AnimationAction {
        SHOW, HIDE,NONE
    }
}
