package com.asgatech.sociallib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AuthenticationDialog extends Dialog {
    private final String request_url;
    private final String redirect_url;
    private AuthenticationListener listener;

    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener, String client_id, String redirect_url, String base_url) {
        super(context);
        this.listener = listener;
        this.redirect_url = redirect_url;
        this.request_url = base_url +
                "oauth/authorize/?client_id=" +
                client_id +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&display=touch&scope=user_profile,user_media";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private void initializeWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                AuthenticationDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String decode = decode(url);
            if (decode.contains("code=")) {
                String code = decode.substring(decode.indexOf("code=") + 5, decode.indexOf("#_"));
                listener.onTokenReceived(code);
                dismiss();
            }
        }
    };


    private static String decode(String url) {

        try {

            String prevURL = "";

            String decodeURL = url;

            while (!prevURL.equals(decodeURL)) {

                prevURL = decodeURL;

                decodeURL = URLDecoder.decode(decodeURL, "UTF-8");

            }

            return decodeURL;

        } catch (UnsupportedEncodingException e) {

            return "Issue while decoding" + e.getMessage();

        }

    }
}
