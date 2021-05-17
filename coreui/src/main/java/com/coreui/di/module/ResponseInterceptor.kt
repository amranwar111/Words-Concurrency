package com.coreui.di.module

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.coredata.module.PreferenceModule
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject
import okhttp3.ResponseBody.Companion.toResponseBody

class ResponseInterceptor @Inject constructor(private val preferenceModule: PreferenceModule) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//        if (!NetworkUtils.isConnected()) return null
        var response: Response? = null

            response = chain.proceed(request)
        val responseString = String(response.body!!.bytes())
//        LogUtils.d("Response: $responseString")
        if (responseString.contains("token_expired", true)) {
//            refreshTokenUsecase.refresh().subscribe()
        } else if (responseString.contains(
                "token_not_provided",
                true
            ) || responseString.contains(
                "user_not_found",
                true
            ) || responseString.contains("token_invalid", true)
        ) {
//            logout.logout().subscribe {
            preferenceModule.removeToken()

            val action = if (preferenceModule.isUser()) {
                "com.eyas.user.login"
            } else {
                "com.eyas.consultant.login"
            }
            val intent = Intent(action)
            ActivityUtils.startActivity(intent)
            ActivityUtils.finishAllActivities()
        }

        val body: ResponseBody = responseString.toResponseBody(response.body?.contentType())
        return response.newBuilder()
            .body(body = body)
            .build()

    }
}