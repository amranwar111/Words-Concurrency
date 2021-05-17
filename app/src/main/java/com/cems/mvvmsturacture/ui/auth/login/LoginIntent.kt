package com.cems.mvvmsturacture.ui.auth.login

import com.common.commondomain.interactor.login.LoginModel
import com.coreui.presentation.base.BaseIntent

sealed class LoginIntent : BaseIntent {
    class loginIntent(val loginModel: LoginModel) : LoginIntent()

}