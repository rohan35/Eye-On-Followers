package com.raydevelopers.sony.eyeonfollowers.models;

import java.io.Serializable;

/**
 * Created by SONY on 23-04-2017.
 */

public class TrackInfo implements Serializable {
    String mUsername;
    String mProfile_pic;
    public TrackInfo(String username,String profile_pic)
    {
        this.mUsername=username;
        this.mProfile_pic=profile_pic;
    }
    public  String getmUsername()
    {
        return mUsername;
    }
    public String getmProfile_pic()
    {
        return mProfile_pic;

    }
}
