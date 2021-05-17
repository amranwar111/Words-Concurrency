package com.asgatech.sociallib;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Android on 2/26/2018.
 */

public class FacebookModule implements ILoginBehavour {


    private ILoginCallback loginCallback;
    private CallbackManager callbackManager;
    private AppCompatActivity activity;

    public void putPermissions(String... permissions) {
        LoginManager.getInstance().logInWithReadPermissions(
                activity,
                Arrays.asList(permissions)
        );
    }

    @Override
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.activity = appCompatActivity;
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void login() {
        putPermissions(SocialModule.getSocialModule().getPermissions());
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
                loginCallback.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                        login();
                    }
                } else {
                    loginCallback.onError(error.getMessage());
                }

            }
        });
    }

    @Override
    public void logout() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }


    private void getUserProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                loginCallback.onLogedIn(response.getJSONObject());
                String email = null;
                try {
                    email = response.getJSONObject().getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Profile profile = Profile.getCurrentProfile();
                DefaultUser userModel = new DefaultUser();
                if (profile != null) {
                    userModel.setEmail(email);
                    userModel.setPic(profile.getProfilePictureUri(200, 200).toString());
                    userModel.setName(profile.getName());
                    userModel.setId(profile.getId());
                    userModel.setType(1);
                }
                loginCallback.onLogedIn(userModel);

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
