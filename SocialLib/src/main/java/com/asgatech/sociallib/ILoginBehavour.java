package com.asgatech.sociallib;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Android on 2/26/2018.
 */

public interface ILoginBehavour {
    public void init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback);

    public void login();

    public void logout();


    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
