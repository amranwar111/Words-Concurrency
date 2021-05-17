package com.asgatech.sociallib;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 2/26/2018.
 */

public class GoogleModule implements ILoginBehavour, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int RC_SIGN_IN = 7;
    private static GoogleApiClient mGoogleApiClient;
    private ILoginCallback loginCallback;
    private ResultCallback<Status> logoutResultCallback;
    private AppCompatActivity activity;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.activity = appCompatActivity;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

//        if(mGoogleApiClient == null)
//            mGoogleApiClient = new GoogleApiClient.Builder((Context) loginCallback)
//                    .enableAutoManage(appCompatActivity, this)
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//        mGoogleApiClient.registerConnectionFailedListener(this);
//        mGoogleApiClient.registerConnectionCallbacks(this);

    }

    @Override
    public void login() {
        logout();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account != null) {

        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            (activity).startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void logout() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account != null) {
            mGoogleSignInClient.signOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {

//        if (result.isSuccessful()) {
        // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
        GoogleSignInAccount acct = null;
        try {
            acct = result.getResult(ApiException.class);

            String personName = acct.getDisplayName();
            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
            String email = acct.getEmail();
            DefaultUser user = new DefaultUser();
            user.setName(personName);
            user.setEmail(email);
            user.setPic(personPhotoUrl);
            user.setId(acct.getId());
            user.setType(3);
            loginCallback.onLogedIn(user);
            try {
                loginCallback.onLogedIn(new JSONObject(new Gson().toJson(user)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
//        } else {
//            loginCallback.onError("failed to get User Information");
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginCallback.onError(connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("gmail", "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("error", "waiting for connection");
    }
}
