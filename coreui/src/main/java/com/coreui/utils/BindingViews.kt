package com.coreui.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.*
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.coreui.R
import com.coreui.di.module.DOMAIN
import com.coreui.utils.validation.GlobalToast
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import com.tripl3dev.prettystates.StatesConstants
import com.tripl3dev.prettystates.setState
import g.trippl3dev.com.validation.domain.isIgnore
import g.trippl3dev.com.validation.domain.isValid
import java.io.File
import java.net.URLConnection
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


//private var target: Target? = null

fun NavController.navigateSafe2(direction: NavDirections, inBackground: Boolean): NavDirections? =
    if (inBackground) {
        direction
    } else {
        currentDestination?.getAction(direction.actionId)?.let { navigate(direction) }
        null
    }


fun NavController.navigateSafe(direction: NavDirections, arg: Bundle?) =
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction.actionId, arg) }

fun NavController.navigateSafe(direction: Int, arg: Bundle?, navOptions: NavOptions) =
    currentDestination?.getAction(direction)?.let { navigate(direction, arg, navOptions) }

fun NavController.navigateSafe(direction: NavDirections) =
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction.actionId) }


fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

@BindingAdapter("statusMargin")
fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment) {
    try {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.name)
            .commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatActivity.replaceFramgmentRemoveIfFound(fragment: Fragment, containerId: Int) {
    try {
        findViewById<View>(containerId)?.setState(StatesConstants.NORMAL_STATE)
        var theFragment = supportFragmentManager.findFragmentByTag(fragment::class.java.name)
        if (theFragment != null)
            supportFragmentManager.beginTransaction().remove(theFragment).commit()
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.name).commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.replaceFramgmentRemoveIfFound(fragment: Fragment, id: Int) {
//    findViewById<View>(id)?.setState(StatesConstants.NORMAL_STATE)
    try {
        var theFragment = childFragmentManager.findFragmentByTag(fragment::class.java.name)
        if (theFragment != null)
            childFragmentManager.beginTransaction().remove(theFragment).commit()
        childFragmentManager.beginTransaction().replace(id, fragment, fragment::class.java.name)
            .commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.replaceFragment(containerId: Int, fragment: Fragment) {
    try {
        childFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment::class.java.name)
            .commit()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


@BindingAdapter("currentTab")
fun setTab(pager: ViewPager, itemLiveData: MutableLiveData<Int>) {
    itemLiveData.value?.let {
        //don't forget to break possible infinite loops!
        if (pager.currentItem != itemLiveData.value) {
            pager.setCurrentItem(itemLiveData.value!!, true)
        }
    }
}

@InverseBindingAdapter(attribute = "currentTab", event = "currentTabAttrChanged")
fun getTab(pager: ViewPager) = pager.currentItem


@BindingAdapter("seeMore")
fun TextView.addSeeMore(color: Int) {
    var line = text.split("\r\n|\r|\n".toRegex())

    var textValue = ""
    if (text.length > 50) {
        tag = text
        setHorizontallyScrolling(false)
        this.setOnKeyListener(null)
        this.isFocusable = false
        isEnabled = true
        layoutDirection = View.LAYOUT_DIRECTION_LTR
        val lines = "${this.text.substring(0, 50)}"?.split("\r\n|\r|\n".toRegex())
//   return  lines.length;

        if (lines.isNotEmpty()) {
            if (lines.size > 2) {
                for (i in 0..2)
                    textValue = "$textValue ${lines[i]}\n"
            } else {
                textValue = "$textValue ${lines[0]}\n"
            }
        } else {
            textValue = "${this.text.substring(0, 50)} "
        }

        text = textValue + "المزيد"
// Style clickable spans based on pattern
        PatternEditableBuilder().addPattern(
            Pattern.compile("المزيد"), color
        ) { text ->
            GlobalToast().showToastInfo(context, tag.toString())
        }.into(this)
    } else if (line.size > 4) {
        tag = text
        setHorizontallyScrolling(false)
        this.setOnKeyListener(null)
        this.isFocusable = false
        isEnabled = true
        layoutDirection = View.LAYOUT_DIRECTION_LTR
        for (i in 0..2)
            textValue = "$textValue ${line[i]}\n"

        text = textValue + "المزيد"
// Style clickable spans based on pattern
        PatternEditableBuilder().addPattern(
            Pattern.compile("المزيد"), color
        ) { text ->
            GlobalToast().showToastInfo(context, tag.toString())
        }.into(this)
    }


}

@BindingAdapter("align")
fun View.align(arabic: Boolean) {
    val layoutParams = layoutParams as RelativeLayout.LayoutParams
    if (arabic) {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
    } else {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
    }
    setLayoutParams(layoutParams)
}


@BindingAdapter("disableUntilHaveDataIn")
fun EditText.disableUntilHaveDataIn(view: View) {
    val anotherEditText = view as EditText
    this@disableUntilHaveDataIn.isEnabled =
        !StringUtils.isTrimEmpty(anotherEditText.text.toString())
    anotherEditText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            this@disableUntilHaveDataIn.isEnabled = !StringUtils.isTrimEmpty(s?.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


@BindingAdapter("rotattoDirection")
fun View.alignusingRotation(arabic: Boolean) {
    rotationY = if (arabic) {
        180f
    } else {
        0f
    }
}

@BindingAdapter("direction")
fun View.direction(arabic: Boolean) {
    if (arabic) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_RTL)
    } else {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR)
    }
}

@BindingAdapter("gravity")
fun View.gravity(arabic: Boolean) {
    if (this is EditText) {
        gravity = if (arabic) {
            Gravity.RIGHT or Gravity.CENTER_VERTICAL
        } else {
            Gravity.LEFT or Gravity.CENTER_VERTICAL
        }
    } else if (this is TextView) {
        gravity = if (arabic) {
            Gravity.RIGHT or Gravity.CENTER_VERTICAL
        } else {
            Gravity.LEFT or Gravity.CENTER_VERTICAL
        }
    } else if (this is LinearLayout) {
        gravity = if (arabic) {
            Gravity.RIGHT or Gravity.CENTER_VERTICAL
        } else {
            Gravity.LEFT or Gravity.CENTER_VERTICAL
        }
    } else if (this is NavigationView) {
        val params = getLayoutParams() as DrawerLayout.LayoutParams
        if (arabic) {
            params.gravity = (Gravity.RIGHT or Gravity.CENTER_VERTICAL)
        } else {
            params.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        }
    }
}

fun String?.isImageFile(): Boolean {
    if (this == null) return false
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.startsWith("image")
}

fun String?.isVideoFile(): Boolean {
    if (this == null) return false
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.startsWith("video")
}

fun String?.isAudio(): Boolean {
    if (this == null) return false
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.startsWith("audio")
}


fun String?.isPdf(): Boolean {
    if (this == null) return false
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && this.contains("pdf")
}

fun String?.isDocx(): Boolean {
    if (this == null) return false
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && (this.contains("docx") || this.contains("doc"))
}

@BindingAdapter("setType")
fun ImageView.setUrlTupe(url: String?) {
    if (StringUtils.isEmpty(url)) {
        return
    }
//    when {
//        url.isImageFile() -> {
////            setImageDrawable(null)
//            setImageResource(R.drawable.ic_baseline_image_24)
//        }
////        url.isVideoFile() -> setImageResource(R.drawable.vedio)
////        url.isAudio() -> setImageResource(R.drawable.play)
//        url.isDocx() -> setImageResource(R.drawable.docx)
//        url.isPdf() -> setImageResource(R.drawable.pdf_docx)
//        else -> setImageResource(R.drawable.docx)

//    }
}

@BindingAdapter("drawableLeftDirection")
fun View.drawable(arabic: Boolean?) {
    var left: Drawable? = null
    if (this is TextView) {
        left = compoundDrawables[0]
        if (arabic!!) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, left, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
        }
    } else if (this is Button) {
        left = compoundDrawables[2]
        if (arabic!!) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, left, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
        }
    }

}


@BindingAdapter("addCounterBelowView", "minLimit", "maxLimit", requireAll = false)
fun EditText.addCounter(below: Int, min: Int = 15, max: Int = 500) {

    val minimum = if (min == 0) 15 else min
    val limit = if (max == 0) 500 else max

    val parent = this.parent
    val addCounterBelowView = (parent as ViewGroup).findViewById<View>(below)
    val textView = TextView(this.context)
    textView.id = 0X552

    (this as EditText).addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            textView.text = "${s?.length} / $limit"
            when {
                s.toString().length < minimum -> textView.setTextColor(
                    ContextCompat.getColor(
                        this@addCounter.context,
                        R.color.accept
                    )
                )
                s.toString().length > limit -> textView.setTextColor(
                    ContextCompat.getColor(
                        this@addCounter.context,
                        R.color.accept
                    )
                )
                else -> textView.setTextColor(
                    ContextCompat.getColor(
                        this@addCounter.context,
                        R.color.blue
                    )
                )
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    })

    when (parent) {
        is ConstraintLayout -> {
            parent.addView(textView)
            val set = ConstraintSet()
            set.clone(parent)
            set.connect(
                textView.id,
                ConstraintSet.LEFT,
                addCounterBelowView.id,
                ConstraintSet.LEFT,
                20
            )
            set.connect(
                textView.id,
                ConstraintSet.TOP,
                addCounterBelowView.id,
                ConstraintSet.BOTTOM,
                10
            )
            set.applyTo(parent)
        }
    }
    textView.text = "${this.text?.toString()?.length} / $limit"
    when {
        this.text?.toString()?.length!! < minimum -> textView.setTextColor(
            ContextCompat.getColor(
                this@addCounter.context,
                R.color.accept
            )
        )
        this.text?.toString()?.length!! > limit -> textView.setTextColor(
            ContextCompat.getColor(
                this@addCounter.context,
                R.color.accept
            )
        )
        else -> textView.setTextColor(
            ContextCompat.getColor(
                this@addCounter.context,
                R.color.accept
            )
        )
    }
}

@BindingAdapter("openBrowser")
fun View.openBrowser(uri: String?) {
    this.setOnClickListener {
        if (uri != null && !uri.isEmpty()) {
            val packageManager = context.packageManager;
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            if (intent.resolveActivity(packageManager) != null) {
                this.context.startActivity(intent)
            }
        }
    }
}

@BindingAdapter("dial")
fun View.dial(uri: String?) {
    this.setOnClickListener {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$uri")
        this.context.startActivity(intent)

    }
}

//if (url != null){
//    var progressBar: ProgressBar? = null
//    if(parent is ViewGroup) {
//        if (   (parent as ViewGroup).findViewById<ProgressBar>(0X022221) == null) {
//            progressBar = ProgressBar(this.context!!,null, android.R.attr.progressBarStyleHorizontal)
//            progressBar.id = 0X022221
//            (parent as ViewGroup).addView(progressBar)
//            progressBar.layoutParams = layoutParams
////
////                if((parent as ViewGroup) is ConstraintLayout){
////                    progressBar.layoutParams = ConstraintLayout.LayoutParams(layoutParams)
////                }else if((parent as ViewGroup) is RelativeLayout){
////                    progressBar.layoutParams = RelativeLayout.LayoutParams(layoutParams)
////                }else if((parent as ViewGroup) is LinearLayout){
////                    progressBar.layoutParams = LinearLayout.LayoutParams(layoutParams)
////                }else if((parent as ViewGroup) is FrameLayout){
////                    progressBar.layoutParams = FrameLayout.LayoutParams(layoutParams)
////                }
//
//        }
//    }
//
//    val progressListener = object : ProgressListener {
//
//        override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
//            val progress = (100 * bytesRead / contentLength).toInt()
//
//            // Enable if you want to see the progress with logcat
//            Log.v("Picasso Looooooading -- > ", "Progress: $progress%");
//            progressBar?.progress = progress
////                if (done) {
////                    Log.i(LOG_TAG, "Done loading")
////                }
//
//        }
//    }
//    val mOkHttpClient = OkHttpClient.Builder().addInterceptor(object: Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val originalResponse = chain.proceed(chain.request())
//            return originalResponse.newBuilder()
//                    .body(ProgressResponseBody(originalResponse.body(), progressListener))
//                    .header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365))
//                    .build()
//        }
//    }).cache(Cache(this.context!!.cacheDir,Long.MAX_VALUE)).build()
//
//    val builder = Picasso.Builder(this.context!!)
//    builder.downloader(OkHttp3Downloader(mOkHttpClient))
//    val built = builder.build()
//    built.setIndicatorsEnabled(true)
//    built.isLoggingEnabled = true
//    built.cancelRequest(this)
//    built.load(if (URLUtil.isValidUrl(url) || URLUtil.isFileUrl(url)) url else "http://104.248.188.131/$url")
//            .resize(400,150)
//            .into(this,object:Callback{
//                override fun onSuccess() {
//                    if (parent is ViewGroup) {
//                        if ((parent as ViewGroup).findViewById<ProgressBar>(0X022221) != null) {
//                            (parent as ViewGroup).removeView((parent as ViewGroup).findViewById<ProgressBar>(0X022221))
//                        }
//                    }
//                }
//
//                override fun onError(e: Exception?) {
//                    if (parent is ViewGroup) {
//                        if ((parent as ViewGroup).findViewById<ProgressBar>(0X022221) != null) {
//                            (parent as ViewGroup).removeView((parent as ViewGroup).findViewById<ProgressBar>(0X022221))
//                        }
//
//                    }
//                }
//
//            })
//}
@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    if (StringUtils.isEmpty(url)) {
        setImageResource(R.drawable.carbon_iconplaceholder)
        return
    }
    Picasso.get()
        .load(if (URLUtil.isValidUrl(url) || URLUtil.isFileUrl(url)) url else "$DOMAIN$url")
        .placeholder(R.drawable.carbon_iconplaceholder)
//                .fit()
        .into(this, object : Callback {
            override fun onSuccess() {
                this@setImage.isIgnore(true)
                this@setImage.isValid(true)
            }

            override fun onError(e: Exception?) {
                this@setImage.isIgnore(false)
                this@setImage.isValid(false)

            }

        })


}

@BindingAdapter("setImageNo")
fun ImageView.setImageNopalce(url: String?) {
    if (StringUtils.isEmpty(url)) {
//        setImageResource(R.drawable.placeholder)
        return
    }
    Picasso.get()
        .load(if (URLUtil.isValidUrl(url) || URLUtil.isFileUrl(url)) url else "$DOMAIN$url")
//        .placeholder(R.drawable.placeholder)
//                .fit()
        .into(this, object : Callback {
            override fun onSuccess() {
                this@setImageNopalce.isIgnore(true)
                this@setImageNopalce.isValid(true)
            }

            override fun onError(e: Exception?) {
                this@setImageNopalce.isIgnore(false)
                this@setImageNopalce.isValid(false)

            }

        })


}

@BindingAdapter("setImageDrawable")
fun ImageView.setImageDrawable(drawable: Drawable?) {
    if (drawable == null) return
    this.setImageDrawable(drawable)
}


@BindingAdapter("setImage", "openInViewer")
fun ImageView.setImageWithOpen(url: String?, open: Boolean) {
    val urle =
        if (URLUtil.isValidUrl(url) || URLUtil.isFileUrl(url)) url else "http://104.248.188.131/$url"
    Picasso.get()
        .load(urle)
//                .fit()
        .into(this)

    this.setOnClickListener {
        val list = arrayOf(urle)
        val view: View =
            LayoutInflater.from(this.context).inflate(R.layout.image_viewer_overlay2, null)
        val dialoge = ImageViewer.Builder<String>(this.context!!, list)
            .setOverlayView(view)
            .hideStatusBar(false)
            .show()
        view?.findViewById<TextView>(R.id.downloadImage).setOnClickListener {
            val downloadManager =
                context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            if (!URLUtil.isValidUrl(urle)) return@setOnClickListener
            var Download_Uri = Uri.parse(urle)
            val urlSections = urle?.split("/")
            val name = urlSections!![urlSections.size - 1]
            val request = DownloadManager.Request(Download_Uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle(" تحميل الملف$name")
            request.setDescription(" تحميل$name")
            request.setVisibleInDownloadsUi(true)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "/Signify/$name"
            )

            var refid = downloadManager.enqueue(request)
        }
        view?.findViewById<ImageView>(R.id.close).setOnClickListener {
            dialoge.onDismiss()
        }

    }
}

fun String.openImage(context: Context) {
    val list = arrayOf(this)
    val view: View = LayoutInflater.from(context).inflate(R.layout.image_viewer_overlay, null)
    val dialoge = ImageViewer.Builder<String>(context!!, list)
        .setOverlayView(view)
        .hideStatusBar(false)
        .show()
    view?.findViewById<TextView>(R.id.download).setOnClickListener {
        val downloadManager =
            context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        if (!URLUtil.isValidUrl(this)) return@setOnClickListener
        var Download_Uri = Uri.parse(this)
        val urlSections = this?.split("/")
        val name = urlSections!![urlSections.size - 1]
        val request = DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle(" تحميل الملف$name")
        request.setDescription(" تحميل$name")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Eyas/$name")

        var refid = downloadManager.enqueue(request)
    }
    view?.findViewById<ImageView>(R.id.close).setOnClickListener {
        dialoge.onDismiss()
    }


}

@BindingAdapter("setAlphaAccording")
fun ImageView.setAlphaAccording(view: EditText) {
    view.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isEmpty()) {
                this@setAlphaAccording.alpha = 0.3f
            } else {
                this@setAlphaAccording.alpha = 1.0f
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    })
}

@BindingAdapter("setColor")
fun TextView.setTheTextColor(color: String?) {
    if (color == null) return
    this.setTextColor(Color.parseColor(color))
}

@BindingAdapter("setDrawableColor")
fun TextView.setDrawableColor(color: Int) {
    compoundDrawablesRelative.forEach {
        if (it != null)
            it.colorFilter = PorterDuffColorFilter(
                color
                , PorterDuff.Mode.SRC_IN
            )
    }
}


//
//@BindingAdapter("setImage","widthImg","heightImg")
//fun ImageView.setImage(url: String?,width: Int,height:Int) {
//    if (url != null)
//        Picasso.get()
//                .load(if (URLUtil.isValidUrl(url) || URLUtil.isFileUrl(url)) url else "http://104.248.188.131/$url")
//                .resize(width,height)
//                .into(this@setImage)
//
//}

@BindingAdapter(value = ["accordignFill", "images"], requireAll = true)
fun ImageView.accordignFill(accordignFill: EditText, images: Pair<Int, Int>) {
    if (accordignFill.text.toString().trim().isEmpty()) {
        this@accordignFill.setImageResource(images.second)
    } else {
        this@accordignFill.setImageResource(images.first)
    }
    accordignFill.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().trim().isEmpty()) {
                this@accordignFill.setImageResource(images.second)
            } else {
                this@accordignFill.setImageResource(images.first)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    })
}

@BindingAdapter("setLineInCenter")
fun TextView.putLineCenterTextView(isCenter: Boolean) {
    this.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

const val SELECTED: Int = 2
const val UNSELECTED: Int = 1
const val INDECATOR: Int = 0

@SuppressLint("ResourceAsColor")
@BindingAdapter("setRatingBarStar")
fun RatingBar.setRatingBarStarYellow(color: Int) {
    var layerDrawable: LayerDrawable = this.progressDrawable as LayerDrawable
    layerDrawable.getDrawable(SELECTED).setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    layerDrawable.getDrawable(UNSELECTED)
        .setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.SRC_ATOP)
    layerDrawable.getDrawable(INDECATOR)
        .setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.SRC_ATOP)
}

@BindingAdapter("bigText", "smallText")
fun TextView.setTextWithBigAndSmallText(big: String, small: String) {
    val overallText = "$big $small"
    val spannable = SpannableStringBuilder()
    spannable.append(overallText)
    spannable.setSpan(
        RelativeSizeSpan(1.5f),
        overallText.indexOf(big),
        overallText.indexOf(big) + big.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        RelativeSizeSpan(0.6f),
        overallText.indexOf(small),
        overallText.indexOf(small) + small.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannable
}

@BindingAdapter("text_color") //customise your name here
fun setTextColor(view: TextView, color: Int) {
    view.setTextColor(color)
}

@BindingAdapter("bigText", "prefix", "postfix", "bigColor")
fun TextView.setTextWithStle(big: String, prefix: String, postfix: String, bidColor: Int) {
    val overallText = "$prefix $big $postfix"
    val spannable = SpannableStringBuilder()
    spannable.append(overallText)
    spannable.setSpan(
        RelativeSizeSpan(1.4f),
        overallText.indexOf(big),
        overallText.indexOf(big) + big.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        ForegroundColorSpan(bidColor),
        overallText.indexOf(big),
        overallText.indexOf(big) + big.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        RelativeSizeSpan(1f),
        overallText.indexOf(prefix),
        overallText.indexOf(prefix) + prefix.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        RelativeSizeSpan(1f),
        overallText.indexOf(postfix),
        overallText.indexOf(postfix) + postfix.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannable
}

@BindingAdapter("coloredText", "prefix", "postfix", "coloredColor", requireAll = false)
fun TextView.setTextWithStyleColor(big: String, prefix: String, postfix: String, bidColor: Int) {
    val overallText = "$prefix $big $postfix"
    val spannable = SpannableStringBuilder()
    spannable.append(overallText)
//    spannable.setSpan(
//        RelativeSizeSpan(1.4f),
//        overallText.indexOf(big),
//        overallText.indexOf(big) + big.length,
//        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//    )
    spannable.setSpan(
        ForegroundColorSpan(bidColor),
        overallText.indexOf(big),
        overallText.indexOf(big) + big.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        RelativeSizeSpan(1f),
        overallText.indexOf(prefix),
        overallText.indexOf(prefix) + prefix.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        RelativeSizeSpan(1f),
        overallText.indexOf(postfix),
        overallText.indexOf(postfix) + postfix.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannable
}

//@BindingAdapter("setTextAndChangeCurrency")
//fun TextView.setTextBigSmall(big: String?) {
//    val sR = context.getString(R.string.SR)
//    val offer = context.getString(R.string.offerItem)
//    if (big == null) return
//    val overallText = "$big"
//    val spannable = SpannableStringBuilder()
//    spannable.append(overallText)
////    spannable.setSpan(AbsoluteSizeSpan(resources.getDimension(R.dimen._7ssp).toInt(), false), overallText.indexOf(big), overallText.indexOf(big)+big.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//    if (big.contains(sR))
//        spannable.setSpan(RelativeSizeSpan(0.7f), overallText.indexOf(sR), overallText.indexOf(sR) + sR.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//    if (big.contains(offer))
//        spannable.setSpan(RelativeSizeSpan(0.7f), overallText.indexOf(offer), overallText.indexOf(offer) + offer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//    this.text = spannable
//}

@BindingAdapter("prefix")
fun TextView.prefix(@SuppressLint("SupportAnnotationUsage") @StringRes resourc: String) {
    this.text = "$resourc ${this.text}"
}

@BindingAdapter("suffix")
fun TextView.suffix(resourc: String) {
    this.text = "${this.text} $resourc"
}
//@BindingAdapter("setBackground")
//fun TextView.setBackground(selected: Boolean) {
//    if (selected)
//        this.background = context.resources.getDrawable(R.drawable.hospital_details_button)
//}

@BindingAdapter("setMandatory")
fun TextView.setMandatory(add: Boolean) {
    if (!add) return

    var textViewText: String = this.text.toString()

    var myText = "*  $textViewText"

    // textViewText= textViewText[0].toString()
    val wordSpan = SpannableString(myText)
    wordSpan.setSpan(ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = wordSpan
}

@BindingAdapter("percent", "color")
fun ProgressBar.setProgressColorForProfile(progress: Int, color: Int) {
    val colorPosition = (progress / 10)
    this.progress = colorPosition
    var progressBarDrawable: LayerDrawable = this.progressDrawable as LayerDrawable
    var progressDrawable: Drawable = progressBarDrawable.getDrawable(1)
    progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}


@BindingAdapter("setRoundBackGround")
fun CheckBox.setRoundBackGround(radius: Float) {
    var stringBuilder: SpannableStringBuilder = SpannableStringBuilder()
    stringBuilder.append(" " + this.text + " ")
    stringBuilder.setSpan(
        RoundedBackgroundSpan(this.context, radius),
        0,
        stringBuilder.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )


    this.text = stringBuilder

}


@BindingAdapter("setCurentdateAndTime")
fun TextView.setCurentdateAndTime(isDate: Boolean) {
    val calendar = Calendar.getInstance()
    val dateAndTime = SimpleDateFormat("yyyy - MM - dd h:mm a", Locale.ENGLISH)
    val date = SimpleDateFormat("yyyy - MM - dd ", Locale.ENGLISH)
    val time = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    val strDate = dateAndTime.format(calendar.time)
    this.text = strDate
}


@BindingAdapter("backgroundDrawableColor")
fun View.setBackgroundDrawableColor(color: Int) {
    val background = background
    when (background) {
        is ShapeDrawable -> background.paint.color = color
        is GradientDrawable -> background.setColor(color)
        is ColorDrawable -> background.color = color
    }
}

@BindingAdapter("convertToStringDate")
fun TextView.convertToStringDate(date: String?) {
    if (date != null) {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val toFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
        try {
            val newDate = format.parse(date)
            this.text = toFormat.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    } else {
        this.text = ""
    }
}

@BindingAdapter("setImageResource")
fun ImageView.setImageResource(image: Int) {
    this.setImageResource(image)
}

@BindingAdapter("fullHeight")
fun View.fullHeight(height: Boolean) {
    if (height) {
        this.layoutParams.height =
            ScreenUtils.getScreenHeight() - resources.getDimension(R.dimen._150sdp).toInt()
    }
}

@BindingAdapter("setImageResourceString")
fun ImageView.setImageResourceString(image: String) {
    val imageInt = this.context.resources.getIdentifier(image, "drawable", this.context.packageName)
    this.setImageResource(imageInt)
}

@BindingAdapter("setImageFile")
fun ImageView.setImageFile(image: File?) {
    if (image != null) {
        Picasso.get().load(image).into(this)
    }
}

@BindingAdapter("setName")
fun TextView.setName(name: Int) {
    this.text = this.context.getString(name)
}

@BindingAdapter("formatPriceEditText")
fun EditText.formatPriceEditText(formate: Boolean) {
    val cleanString = this.text.replace("[$,.]".toRegex(), "")
    val formattedText = cleanString?.formatPrice()
    this@formatPriceEditText.setText(formattedText)
    this@formatPriceEditText.setSelection(formattedText?.length!!)
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            this@formatPriceEditText.removeTextChangedListener(this)
            val cleanString = s.toString().replace("[$,.]".toRegex(), "")
            val formattedText = cleanString?.formatPrice()
            this@formatPriceEditText.setText(formattedText)
            this@formatPriceEditText.setSelection(formattedText?.length!!)
            this@formatPriceEditText.addTextChangedListener(this)
        }

    })
}

@BindingAdapter("setPrice")
fun TextView.setPrice(price: String) {
    val nf_us = NumberFormat.getInstance(Locale.ENGLISH)
    val number_us = nf_us.format(java.lang.Double.parseDouble(price))
    this.text = number_us
}

fun View.setViewState(xmlLayout: Int, colorRes: Int, hide: Boolean) {
    val view: View = LayoutInflater.from(context).inflate(xmlLayout, null)
    val loadingContainer = LinearLayout(context)
    loadingContainer.layoutParams = this.layoutParams
    loadingContainer.setBackgroundColor(ContextCompat.getColor(context, colorRes))
    loadingContainer.addView(view)
    loadingContainer.id = this.id - 1
    loadingContainer.gravity = Gravity.CENTER
    (this.parent as ViewGroup).addView(loadingContainer)
    this.visibility = if (hide) View.INVISIBLE else View.VISIBLE
}


fun View.setBorder(color: Int, width: Int) {
    val shape = GradientDrawable()
    shape.setShape(GradientDrawable.RECTANGLE);
    shape.cornerRadii = floatArrayOf(8f, 8f, 8f, 8f, 0f, 0f, 0f, 0f)
//    shape.setColor(color);
    shape.setStroke(3, color)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        this.setBackgroundDrawable(shape)
    } else {
        this.background = shape
    }
}

fun NestedScrollView.setLister(
    listener: RecyclerView.OnScrollListener,
    recyclerView: RecyclerView
) {
    this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
    { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        listener.onScrolled(recyclerView, scrollX, scrollY)
    })



    fun ProgressBar.changeColor(color: Int) {
        var progressBarDrawable: LayerDrawable = this.progressDrawable as LayerDrawable
        var progressDrawable: Drawable = progressBarDrawable.getDrawable(1)
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)

    }


}

fun Context.getActivity(): AppCompatActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = (context as ContextWrapper).baseContext
    }
    return null
}

interface OnImageViewSizeChanged {
    operator fun invoke(v: ImageView, w: Int, h: Int)
}

fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
    var image = this
    if (maxHeight > 0 && maxWidth > 0) {
        val width = image.width
        val height = image.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        return image
    } else {
        return image
    }
}


fun String.formatPrice(): String? {
    if (StringUtils.isEmpty(this)) return ""
    val nf_us = NumberFormat.getInstance(Locale.ENGLISH)
    val number_us = nf_us.format(java.lang.Double.parseDouble(this))
    return number_us
}

fun formatPrice(price: Double): String? {
    val nf_us = NumberFormat.getInstance(Locale.ENGLISH)
    val number_us = nf_us.format(price)
    return number_us
}

fun formatPriceOrNum(price: Int): String? {
    return NumberFormat.getNumberInstance(Locale.ENGLISH).format(price)
}


fun String.convertToStringDate(): String? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val toFormat = SimpleDateFormat("yyyy/MM/dd  hh:mm aa", Locale.ENGLISH)
    try {
        val newDate = format.parse(this)
        return toFormat.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.convertToStringDateOnlyBaseFormat(): String? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val toFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val newDate = format.parse(this)
        return toFormat.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.clearStringFromPrice(): String {
    return this.replace("[$,.]".toRegex(), "")
}

fun String.convertToStringDateOnly(): String? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val toFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
    try {
        val newDate = format.parse(this)
        return toFormat.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null


}

fun String.getMediaDuration(): String {
    var time = ""
    Thread {
        val retriever = MediaMetadataRetriever()
        val url = if (URLUtil.isValidUrl(this) || URLUtil.isFileUrl(this)) this else "$DOMAIN$this"
        Log.e("url", url)
        retriever.setDataSource(url, HashMap<String, String>())
        time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
    }.run()
    return time
}
