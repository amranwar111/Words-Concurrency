package app


import com.coreui.CoreApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarketerApplication : CoreApp() {

//    val appComponent: AppComponent by lazy {
//        initializeComponent()
//    }
//
//    open fun initializeComponent(): AppComponent {
//        // Creates an instance of AppComponent using its Factory constructor
//        // We pass the applicationContext that will be used as Context in the graph
//        return DaggerAppComponent.factory().create(applicationContext)
//    }
//    override fun applicationInjector(): AndroidInjector<out dagger.android.DaggerApplication> {
//        val component = DaggerAppComponent.builder().application(this).build()
//        component.inject(this)
//        return component
//    }

}

