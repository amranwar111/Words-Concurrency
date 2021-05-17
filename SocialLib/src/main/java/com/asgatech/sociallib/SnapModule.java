package com.asgatech.sociallib;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.core.controller.LoginStateController;
import com.snapchat.kit.sdk.login.models.MeData;
import com.snapchat.kit.sdk.login.models.UserDataResponse;
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback;


/**
 * Created by Android on 2/26/2020.
 */
public class SnapModule implements ILoginBehavour {
    private ILoginCallback loginCallback;
    private AppCompatActivity activity;

    @Override
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.activity = appCompatActivity;

        LoginStateController.OnLoginStateChangedListener mLoginStateChangedListener = new LoginStateController.OnLoginStateChangedListener() {
            @Override
            public void onLoginSucceeded() {

            }

            @Override
            public void onLoginFailed() {

            }

            @Override
            public void onLogout() {

            }
        };

        SnapLogin.getLoginStateController(activity).addOnLoginStateChangedListener(mLoginStateChangedListener);
    }

    @Override
    public void login() {
        initSnapChat();
    }

    @Override
    public void logout() {
        SnapLogin.getAuthTokenManager(activity).revokeToken();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


//    @Override
//    public void onLoginSucceeded() {
//        fetchUserData();
//    }
//
//    @Override
//    public void onLoginFailed() {
//        loginCallback.onError("onLoginFailed");
//
//    }
//
//    @Override
//    public void onLogout() {
//
//    }

    private void initSnapChat() {
        SnapLogin.getAuthTokenManager(activity).startTokenGrant();
    }

    private void fetchUserData() {
        String query = "{me{bitmoji{avatar},displayName,externalId}}";
        SnapLogin.fetchUserData(activity, query, null, new FetchUserDataCallback() {
            @Override
            public void onSuccess(@Nullable UserDataResponse userDataResponse) {
                if (userDataResponse == null || userDataResponse.getData() == null) {
                    return;
                }

                MeData meData = userDataResponse.getData().getMe();
                if (meData == null) {
                    return;
                }
                DefaultUser userModel = new DefaultUser();
                userModel.setName(meData.getDisplayName());
                userModel.setId(meData.getExternalId());
                userModel.setPic(meData.getBitmojiData().getAvatar());
                userModel.setType(5);
                loginCallback.onLogedIn(userModel);
                System.out.println(new Gson().toJson(meData));
//                SnapLogin.getLoginStateController(activity).removeOnLoginStateChangedListener(this);
            }

            @Override
            public void onFailure(boolean isNetworkError, int statusCode) {
                loginCallback.onError(statusCode + "");

            }
        });
    }
}
