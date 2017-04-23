package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.raydevelopers.sony.eyeonfollowers.R;
import com.raydevelopers.sony.eyeonfollowers.fetchers.GetAuthCode;

/**
 * Created by SONY on 23-04-2017.
 */

public  class InstagramLoginDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(
                R.layout.intagram_login_fragment, container, false);

        getDialog().setTitle("Loading...");
        setCancelable(false);
        String authURLString=NetworkUtils.buildUrl(getContext()).toString();
        WebView webView =(WebView)rootView.findViewById(R.id.webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient(getContext()));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authURLString);
        return rootView;
    }
    public class AuthWebViewClient extends WebViewClient {
        private Context mContext;
        public AuthWebViewClient(Context context)
        {
            this.mContext=context;
        }
        String request_token;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl().toString().startsWith(NetworkUtils.CALLBACK_URL))
            {
                String parts[] = request.getUrl().toString().split("=");
                request_token = parts[1];  //This is your request token.
                setPreferences();

                InstagramLoginDialog.this.dismiss();
                new GetAuthCode(getContext()).fetchAuthCode();


                return true;
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(NetworkUtils.CALLBACK_URL))
            {
                String parts[] = url.split("=");
                request_token = parts[1];  //This is your request token.
                setPreferences();
                InstagramLoginDialog.this.dismiss();
                new GetAuthCode(getContext()).fetchAuthCode();
                return true;
            }
            return false;
        }

        public void setPreferences()
        {
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(getString(R.string.request_token),request_token);
            editor.commit();
        }
    }

}
