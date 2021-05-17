package com.asgatech.sociallib;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Android on 2/26/2020.
 */
public class InstagramModule implements ILoginBehavour, AuthenticationListener {
    private ILoginCallback loginCallback;
    private AppCompatActivity activity;
    private AuthenticationDialog authenticationDialog = null;
    private String client_id, client_secret, redirect_url, base_url, get_user_info_url;

    private void prepareMetaData(AppCompatActivity activity) {
        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            client_id = bundle.getString("client_id");
            client_secret = bundle.getString("client_secret");
            redirect_url = bundle.getString("redirect_url");
            base_url = bundle.getString("base_url");
            get_user_info_url = bundle.getString("get_user_info_url");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("error", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("error", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    @Override
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.activity = appCompatActivity;
        prepareMetaData(activity);

    }

    @Override
    public void login() {

        authenticationDialog = new AuthenticationDialog(activity, this, client_id, redirect_url, base_url);
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();

    }

    @Override
    public void logout() {
//        if (AccessToken.getCurrentAccessToken() != null) {
//
//        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onTokenReceived(String auth_token) {
        getUserInfoByAccessToken(auth_token);
    }


    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI(token).execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {
        String code;

        public RequestInstagramAPI(String code) {
            this.code = code;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(get_user_info_url);

            try {
                List<NameValuePair> sendParams = new ArrayList<NameValuePair>();
                sendParams.add(new BasicNameValuePair("client_id", client_id));
                sendParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
                sendParams.add(new BasicNameValuePair("redirect_uri", redirect_url));
                sendParams.add(new BasicNameValuePair("client_secret", client_secret));
                sendParams.add(new BasicNameValuePair("code", code));

                httpPost.setEntity(new UrlEncodedFormEntity(sendParams));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    loginCallback.onLogedIn(jsonObject);
                    DefaultUser userModel = new DefaultUser();
                    userModel.setId(jsonObject.getString("user_id"));
                    userModel.setType(4);
                    userModel.setAccess_token(jsonObject.getString("access_token"));
                    loginCallback.onLogedIn(userModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

            }
        }
    }
}
