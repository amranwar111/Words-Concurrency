package com.cems.mvvmsturacture.ui.auth.login


import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.cems.mvvmsturacture.R
import com.cems.mvvmsturacture.databinding.FragmentLoginBinding
import com.common.commondomain.diinterfaces.AuthScope
import com.common.commondomain.interactor.login.LoginModel
import com.coredata.module.PreferenceModule
import com.coredomain.BaseVS
import com.coreui.ui.fragment.BaseFragment
import com.coreui.utils.GlobalToastNav
import com.coreui.utils.navigateSafe
import com.coreui.utils.navigateSafe2
import com.coreui.utils.validation.ValidationTypes
import dagger.hilt.android.AndroidEntryPoint
import g.trippl3dev.com.validation.domain.interfaces.ValidationChecker
import g.trippl3dev.com.validation.domain.interfaces.ValidationModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, LoginIntent>(), View.OnClickListener {
    @Inject
    lateinit var preferenceModule: PreferenceModule
    val stringList = listOf("one", "two", "three", "four", "five")

    companion object {
        const val Validation = "Validation"
    }

    private val signUpDirection = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
    private val loginPublisher = BehaviorSubject.create<LoginIntent.loginIntent>()
    private lateinit var binding: FragmentLoginBinding
    override fun intents(): Observable<LoginIntent> = loginPublisher as Observable<LoginIntent>

    override fun getFragmentVM(): Class<out LoginViewModel> = LoginViewModel::class.java

    override fun render(state: BaseVS) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)

        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.model = vm.userLoginModel
        Log.e("kasjudilul", preferenceModule.getUserType())
        vm.loginError.observe(
            viewLifecycleOwner,
            Observer { if (it.isNotEmpty()) errorMessage(it) })
        vm.loginEvent.observe(viewLifecycleOwner, Observer { if (it) login() })
        binding.onClickListener = this
        vm.setUsers()

        Observable.fromIterable(vm.userLoginModelList)
//            .toList()
//            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ o -> Log.e("s;dl;k", o.toString()) }, { e -> Log.e("s;dl;k", e.message) })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navControl = Navigation.findNavController(view)
        navControl.addOnDestinationChangedListener { _, destination, _ ->
            if (!destination.label!!.equals("ToastFragment")) {
//                Blurry.delete(binding.content)
            }
        }
        enableValidation()
    }

    var validation: ValidationModule? = null
    private fun enableValidation() {
        validation = ValidationModule.get()
            .addType(ValidationTypes.MEditText.TYPE, ValidationTypes.MEditText())
//            .setToastMethod(GlobalToast())
            .setOverallValidationChecker(object : ValidationChecker.OverallValidationChecker {
                override fun onNotValidError(error: String?, view: View?) {


                        //                        navDirection(direction)
                        vm.loginError(error!!)
                        vm.userLoginModel.phone = "alkfdoj"


                }

                override fun onValid() {
                    binding.buttonLogin.isEnabled = true
                }

                override fun onNotValid() {
                    binding.buttonLogin.isEnabled = true
                }
            })
        binding.executePendingBindings()
        validation?.enableWatching(binding.root)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_login -> {
//                SocialModule.getSocialModule().setLoginType(SocialModule.Socials.SNAPCHAT)
//                SocialModule.getSocialModule().login()

                if (validation?.validate(binding.root) != false) {
                    vm.loginEvent()
                }


            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onMessageEvent(event: String) {
        when (event) {
            Validation -> {
            }
        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun errorMessage(msg: String) {
//        Blurry.with(activity)
//            .radius(10)
//            .sampling(8)
//            .async()
////            .animate(500)
//            .onto(binding.content as ViewGroup)
//        val imageComposer = Blurry.with(activity!!).capture(binding.content as ViewGroup)
//        imageComposer.into(binding.img)

        val direction =
            LoginFragmentDirections.actionLoginFragmentToToastFragment(
                GlobalToastNav().toastWarn(
                    msg, Validation
                )
            )
        navControl.navigateSafe(direction)

        vm.loginErrorComplete()
    }

    private fun login() {
        Log.e("LAKJKW", ";JEIW")
        loginPublisher.onNext(LoginIntent.loginIntent(LoginModel().apply {
            this.username = vm.userLoginModel.phone
            this.password = vm.userLoginModel.password
            this.countryId = vm.userLoginModel.countryId
        }))
        vm.loginEventComplete()
    }


}
