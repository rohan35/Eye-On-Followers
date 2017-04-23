package com.raydevelopers.sony.eyeonfollowers.fetchers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raydevelopers.sony.eyeonfollowers.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 23-04-2017.
 */


public class GetUserData  {
    Context mContext;
    OnUserDataReceived mOnUserDataReceived;
    public GetUserData(Context c,OnUserDataReceived onUserDataReceived)

    {
        this.mContext=c;
        this.mOnUserDataReceived=onUserDataReceived;
    }
    public void fetchdata()
    {
        mOnUserDataReceived.preRetreiving();
        String url= NetworkUtils.buildUrl(mContext,1).toString();
        System.out.println(url);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            JSONObject data=jsonObject.getJSONObject("data");


                            mOnUserDataReceived.onDataRetreived(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(stringRequest);
    }}