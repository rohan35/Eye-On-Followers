package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.raydevelopers.sony.eyeonfollowers.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SONY on 23-04-2017.
 */


public class NetworkUtils {

    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL ="https://api.instagram.com/oauth/access_token";
    public static final String API_URL = "https://api.instagram.com/v1";
    public static String CALLBACK_URL = "https://rohan35.github.io/instagram_callback";
    public static  String CLIENT_ID_STRING="client_id";
    public static String CALLBACK_URL_STRING="redirect_uri";
    private static String RESPONSE_TYPE_STRING="response_type";
    public static String RESPONSE_TYPE="code";






    public static URL buildUrl(Context context, Integer... type_id)
    {
        Uri uri=null;
        URL url=null;
        String accessToken="";
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getString(context.getString(R.string.access_token),null)!=null) {
            accessToken = sharedPreferences.getString(context.getString(R.string.access_token),null);
        }
        if(type_id.length>0&&type_id[0]==0)
        {

            uri = Uri.parse(TOKEN_URL).buildUpon()
                    .build();
        }
        else if(type_id.length>0&&type_id[0]==1)
        {
            uri = Uri.parse(API_URL).buildUpon().appendPath(context.getString(R.string.users_string))
                    .appendPath(context.getString(R.string.self_string)).
                    appendQueryParameter(context.getString(R.string.access_token),accessToken)
                    .build();
        }
        else if(type_id.length>0&&type_id[0]==2)
        {
            uri = Uri.parse(API_URL).buildUpon().appendPath(context.getString(R.string.users_string))
                    .appendPath(context.getString(R.string.self_string)).appendPath(context.getString(R.string.follows)).
                    appendQueryParameter(context.getString(R.string.access_token),accessToken)
                    .build();
        }
        else if(type_id.length>0&&type_id[0]==3)
        {
            uri = Uri.parse(API_URL).buildUpon().appendPath(context.getString(R.string.users_string))
                    .appendPath(context.getString(R.string.self_string)).appendPath("followed-by").
                    appendQueryParameter(context.getString(R.string.access_token),accessToken)
                    .build();
        }


        else {
            uri = Uri.parse(AUTH_URL).buildUpon().appendQueryParameter(CLIENT_ID_STRING,context.getString(R.string.client_id)).
                    appendQueryParameter(CALLBACK_URL_STRING, CALLBACK_URL).appendQueryParameter(RESPONSE_TYPE_STRING, RESPONSE_TYPE)
                    .appendQueryParameter(context.getString(R.string.scope_string),
                            context.getString(R.string.followers_list_string))
                    .build();
        }
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}