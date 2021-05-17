package com.asgatech.sociallib;

import android.content.Intent;


import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Android on 2/26/2018.
 */

public class SocialModule {
    private static SocialModule socialModule;
    private ILoginBehavour loginBehavour;
    private AppCompatActivity appCompatActivity;
    private ILoginCallback loginCallback;
    private String[] permissions;

    public static SocialModule getSocialModule() {
        if (socialModule == null)
            socialModule = new SocialModule();
        return socialModule;
    }

    private TwitterModule twitterModule;
    private InstagramModule instagramModule;

    public SocialModule setLoginType(@Socials int loginType) {
        switch (loginType) {
            case Socials.FACEBOOK:
                loginBehavour = new FacebookModule();
                loginBehavour.init(appCompatActivity, loginCallback);
                break;
            case Socials.GOOGLE:
                loginBehavour = new GoogleModule();
                loginBehavour.init(appCompatActivity, loginCallback);
                break;
            case Socials.TWITTER:
                if (twitterModule == null) {
                    twitterModule = new TwitterModule();
                    twitterModule.init(appCompatActivity, loginCallback);
                }
                loginBehavour = twitterModule;
                break;

            case Socials.INSTAGRAM:
                if (instagramModule == null) {
                    instagramModule = new InstagramModule();
                    instagramModule.init(appCompatActivity, loginCallback);
                }
                loginBehavour = instagramModule;
                break;
            case Socials.SNAPCHAT:
                loginBehavour = new SnapModule();
                loginBehavour.init(appCompatActivity, loginCallback);
                break;
        }

        logout();
        return this;
    }


    public SocialModule init(AppCompatActivity appCompatActivity, ILoginCallback loginCallback, int... socialTypes) {
        this.appCompatActivity = appCompatActivity;
        this.loginCallback = loginCallback;
        List loginType = Collections.singletonList(socialTypes);
        if (loginType.contains(Socials.TWITTER) && twitterModule == null) {
            twitterModule = new TwitterModule();
            twitterModule.init(appCompatActivity, loginCallback);
        }

        if (loginType.contains(Socials.INSTAGRAM) && instagramModule == null) {
            instagramModule = new InstagramModule();
            instagramModule.init(appCompatActivity, loginCallback);
        }


        return this;
    }


    public void login() {
        loginBehavour.login();
    }


    public void logout() {
        loginBehavour.logout();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (loginBehavour != null)
            loginBehavour.onActivityResult(requestCode, resultCode, data);
    }



    public SocialModule setPermissionForFacebook(String... permissionForFacebook) {
        this.permissions = permissionForFacebook;
        return this;
    }

    public String[] getPermissions() {
        return permissions;
    }

    @IntDef({Socials.FACEBOOK, Socials.TWITTER, Socials.GOOGLE, Socials.INSTAGRAM, Socials.SNAPCHAT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Socials {
        int FACEBOOK = 0;
        int TWITTER = 1;
        int GOOGLE = 2;
        int INSTAGRAM = 3;
        int SNAPCHAT = 4;
    }
}
