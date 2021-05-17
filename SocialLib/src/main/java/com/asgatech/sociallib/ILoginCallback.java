package com.asgatech.sociallib;

import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

/**
 * Created by Android on 2/26/2018.
 */

public interface ILoginCallback {
    public void onLogedIn(DefaultUser defaultUser);

    public default void onLogedIn(JSONObject response) {

    }

    default void onError(String error) {

    }

    default void onCancel() {

    }


    default void onLogout(Status status) {

    }
}
