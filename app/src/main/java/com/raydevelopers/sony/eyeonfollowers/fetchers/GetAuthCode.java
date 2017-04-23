package com.raydevelopers.sony.eyeonfollowers.fetchers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raydevelopers.sony.eyeonfollowers.MainActivity;
import com.raydevelopers.sony.eyeonfollowers.R;
import com.raydevelopers.sony.eyeonfollowers.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SONY on 23-04-2017.
 */

public class GetAuthCode {
    private static String CLIENT_SECRET="4d8450868b52459dbf147f04677abf6b";
    private static String CLIENT_SECRET_STRING="client_secret";
    private static String Grant_TYPE_STRING="grant_type";
    private static String Grant_TYPE="authorization_code";
    private Context mContext;
    public GetAuthCode(Context c)
    {
        this.mContext=c;
    }
    public void fetchAuthCode()
    {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String request_token=sharedPref.getString(mContext.getString(R.string.request_token),"");
        Log.d(mContext.getString(R.string.request_token),request_token);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,NetworkUtils.buildUrl(mContext,0).toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject userInfo=jsonObject.getJSONObject("user");
                            sharedPref.edit().remove(mContext.getString(R.string.request_token)).commit();
                            SharedPreferences.Editor editor=sharedPref.edit();
                            editor.putString(mContext.getString(R.string.access_token),
                                    jsonObject.getString(mContext.getString(R.string.access_token)));
                            editor.commit();
                            Intent i=new Intent(mContext, MainActivity.class);
                            mContext.startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(NetworkUtils.CLIENT_ID_STRING,mContext.getString(R.string.client_id));
                params.put(CLIENT_SECRET_STRING,CLIENT_SECRET);
                params.put(Grant_TYPE_STRING, Grant_TYPE);
                params.put(NetworkUtils.CALLBACK_URL_STRING,NetworkUtils.CALLBACK_URL);
                params.put(NetworkUtils.RESPONSE_TYPE,request_token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
}
