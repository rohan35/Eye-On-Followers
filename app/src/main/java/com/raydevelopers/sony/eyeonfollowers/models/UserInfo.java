package com.raydevelopers.sony.eyeonfollowers.models;

/**
 * Created by SONY on 23-04-2017.
 */

public class UserInfo {
    String mUsername;
    String mProfile_pic;
    String mFullname;
    String mId;
    public UserInfo(String username,String profilePic,String fullName,String id)
    {
        this.mUsername=username;
        this.mProfile_pic=profilePic;
        this.mFullname=fullName;
        this.mId=id;
    }
    public  String getmUsername()
    {
        return mUsername;
    }
    public String getmProfile_pic()
    {
        return mProfile_pic;

    }
    public String getmFullname()
    {
        return mFullname;

    }
    public String getmId()
    {
        return mId;
    }
}
