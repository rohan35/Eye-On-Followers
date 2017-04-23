package com.raydevelopers.sony.eyeonfollowers.fetchers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raydevelopers.sony.eyeonfollowers.models.UserInfo;
import com.raydevelopers.sony.eyeonfollowers.utils.NetworkUtils;
import com.raydevelopers.sony.eyeonfollowers.utils.SetTrackingDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SONY on 23-04-2017.
 */

public class GetTrackingDetails {
    private Context mContext;
    private SQLiteDatabase db;
    OnTrackingDataReceived mOnTrackingDataReceived;
    Uri mNewUri;
    ContentValues mNewValues = new ContentValues();
    ArrayList<UserInfo> followers=new ArrayList<>();
    ArrayList<UserInfo> follows=new ArrayList<>();

    private String[] urls=new String[2];
    public GetTrackingDetails(Context c, OnTrackingDataReceived onTrackingDataReceived)
    {
        this.mContext=c;
        this.mOnTrackingDataReceived=onTrackingDataReceived;
    }
    public void fetchdetails()
    {

        urls[0]= NetworkUtils.buildUrl(mContext,2).toString();
        urls[1] =NetworkUtils.buildUrl(mContext,3).toString();
        System.out.println(urls[0]);
        System.out.println(urls[1]);
        for(int i=0;i<2;i++)
        {
            multipleCalls(urls[i]);
        }


    }

    public void multipleCalls(final String url)
    {
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        final StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET,url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray data=jsonObject.getJSONArray("data");
                            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
                            SharedPreferences.Editor editor = sharedPreferences.edit();


                            if(url.equals(urls[0])) {

//editor.remove("follows").apply();
                                editor.putString("follows",jsonObject.toString());
                                //System.out.println(jsonObject.toString());


                            }
                            else
                            {
                                new SetTrackingDetails(mContext).setGainedLostUsers(jsonObject);
                                //  editor.remove("followedBy").apply();

                                editor.putString("followedBy",jsonObject.toString());

                                // System.out.println(jsonObject.toString());


                            }

                            editor.apply();
                            mOnTrackingDataReceived.onDataRetreived();




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
    }

}

