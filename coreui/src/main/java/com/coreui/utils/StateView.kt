package com.coreui.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.util.Property
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import java.util.*

class StateView(private val context: Context) {
    private var container: ViewGroup? = null
    private var boundaries: View? = null
    internal var animators: MutableCollection<Animator>? = null
    private var imageRes: Int = 0
    private var loadingId: Int = 0
    private var colorRes: Int = 0
    private val image: ImageView
    private var hideBoundariesView: Boolean = false

    init {
        image = ImageView(context)
    }

    fun setContainer(viewGroup: ViewGroup): StateView {
        this.container = viewGroup
        return this
    }

    fun setClickListener(clickListener: View.OnClickListener): StateView {
        image.setOnClickListener(clickListener)
        return this
    }

    fun setBoundariesView(view: View): StateView {
        this.boundaries = view
        return this
    }

    fun setLoadingImage(imageRes: Int): StateView {
        this.imageRes = imageRes
        return this
    }

    fun addAnimator(item: Property<View, Float>, start: Float, value: Float): StateView {
        if (this.animators == null) animators = ArrayList()
        animators!!.add(AnimationUtils.propertyAnimation(image, item, start, value))
        return this
    }

    fun setLoadinScrimColor(colorRes: Int): StateView {
        this.colorRes = colorRes
        return this
    }

    fun setLoadingId(loadingId: Int): StateView {
        this.loadingId = loadingId
        return this
    }

    fun hideBoundariesView(hide: Boolean?): StateView {
        this.hideBoundariesView = hide!!
        return this
    }

    fun show() {
        hideState()
        val padding = DimensionUtils.convertDPToPX(context, 20)
        image.setPadding(padding, padding, padding, padding)
        val params = LinearLayout.LayoutParams(DimensionUtils.convertDPToPX(context, 170), DimensionUtils.convertDPToPX(context, 170))
        image.layoutParams = params
        val loadingContainer = LinearLayout(context)
        loadingContainer.layoutParams = boundaries!!.layoutParams
        loadingContainer.setBackgroundColor(ContextCompat.getColor(context, colorRes))
        loadingContainer.addView(image)
        loadingContainer.id = loadingId
        loadingContainer.gravity = Gravity.CENTER
        container!!.addView(loadingContainer)
        image.setImageResource(imageRes)
        boundaries!!.visibility = if (hideBoundariesView) View.INVISIBLE else View.VISIBLE
        val set = AnimatorSet()
        set.playTogether(animators)
        set.start()
    }


    fun hideState() {
        container!!.removeView(container!!.findViewById(loadingId))
    }

    companion object {

        operator fun get(context: Context): StateView {
            return StateView(context)
        }
    }
}