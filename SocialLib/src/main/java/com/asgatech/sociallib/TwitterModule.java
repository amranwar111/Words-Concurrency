package com.asgatech.sociallib;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;

/**
 * Created by Android on 2/26/2018.
 */

public class TwitterModule implements ILoginBehavour {
    private ILoginCallback loginCallback;
    private String twitterKey;
    private String twitterSecret;
    private TwitterAuthClient mTwitterAuthClient;
    private AppCompatActivity activity;

    public void prepareMetaData(AppCompatActivity activity) {
        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            twitterKey = bundle.getString("twitter_key");
            twitterSecret = bundle.getString("twitter_secret");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("error", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    public void prepareConfig(AppCompatActivity appCompatActivity) {
        prepareMetaData(activity);
        TwitterConfig config = new TwitterConfig.Builder(appCompatActivity)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(twitterKey, twitterSecret))
                .debug(true)
                .build();

        Twitter.initialize(config);
    }

    @Override
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.activity = appCompatActivity;
        Twitter.initialize(activity);
        prepareConfig(activity);
        mTwitterAuthClient = new TwitterAuthClient();
    }

    @Override
    public void login() {
        mTwitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                getUserProfile(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                loginCallback.onError(exception.getMessage());
            }
        });

    }

    private void getUserProfile(TwitterSession data) {
        Call<User> userResult = TwitterCore.getInstance().getApiClient(data).getAccountService().verifyCredentials(true, true, true);
        userResult.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User user = result.data;
                DefaultUser defaultUser = new DefaultUser();
                defaultUser.setId(user.idStr);
                defaultUser.setName(user.name);
                defaultUser.setEmail(user.email);
                defaultUser.setPic(user.profileImageUrlHttps);
                defaultUser.setType(2);
                loginCallback.onLogedIn(defaultUser);
                try {
                    loginCallback.onLogedIn(new JSONObject(new Gson().toJson(user)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                loginCallback.onError(exception.getMessage());
            }
        });
    }

    @Override
    public void logout() {
        mTwitterAuthClient.cancelAuthorize();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);

    }


}
