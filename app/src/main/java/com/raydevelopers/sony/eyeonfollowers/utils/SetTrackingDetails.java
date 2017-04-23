package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.raydevelopers.sony.eyeonfollowers.MainActivity;
import com.raydevelopers.sony.eyeonfollowers.fetchers.GetTrackingDetails;
import com.raydevelopers.sony.eyeonfollowers.fetchers.OnTrackingDataReceived;
import com.raydevelopers.sony.eyeonfollowers.models.TrackInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.raydevelopers.sony.eyeonfollowers.MainActivity.dialog;

/**
 * Created by SONY on 23-04-2017.
 */

public class SetTrackingDetails {
    Context mContext;
    static List<String> nonFollowers,fans,common;
    static JSONArray followsListArray;
    static  JSONArray followedByListArray;
    static JSONArray followedByListArrayOld;
    static JSONArray followedByListArrayNew;
    ArrayList<TrackInfo> arrayListNonFollowers=new ArrayList<>();
    ArrayList<TrackInfo> arrayListFans=new ArrayList<>();
    ArrayList<TrackInfo> arrayListMutual=new ArrayList<>();
    static ArrayList<String> followedByListNew=new ArrayList<>();
    static ArrayList<String> followedByList=new ArrayList<>();

    public SetTrackingDetails(Context c)
    {
        this.mContext=c;
    }
    public void getTrackCount() throws JSONException {
        getTrackingData().fetchdetails();

    }
    public GetTrackingDetails getTrackingData() {
        return new GetTrackingDetails(mContext, new OnTrackingDataReceived() {

            @Override
            public void onDataRetreived()throws JSONException {
                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
                String strFollowsList = sharedPreferences.getString("follows",null);
                String strFollowedByList=sharedPreferences.getString("followedBy",null);


                if(strFollowsList!=null&&strFollowedByList!=null)
                {
                    JSONObject followsListObject=new JSONObject(strFollowsList);
                    JSONObject followedByListObject=new JSONObject(strFollowedByList);
                    followsListArray=followsListObject.getJSONArray("data");
                    followedByListArray=followedByListObject.getJSONArray("data");
                    System.out.println(followedByListArray);
                    ArrayList<String> followsList=new ArrayList<>();
                    ArrayList<String> followedByList=new ArrayList<>();
                    for (int i = 0; i < followsListArray.length(); i++) {
                        JSONObject iteratorObject = followsListArray.getJSONObject(i);

                        followsList.add(i,iteratorObject.getString("username"));

                    }
                    for (int i = 0; i < followedByListArray.length(); i++) {
                        JSONObject iteratorObject = followedByListArray.getJSONObject(i);
                        followedByList.add(i,iteratorObject.getString("username"));
                    }

                    Collection<String> listOne = new ArrayList((followsList));
                    Collection<String> listTwo = new ArrayList((followedByList));
                    nonFollowers = new ArrayList<String>(listOne);
                    fans = new ArrayList<String>(listTwo);
                    common = new ArrayList<String>(listOne);
                    common.retainAll(listTwo);

                    nonFollowers.removeAll( listTwo );
                    fans.removeAll( listOne );
                    MainActivity.nonFollowersCount.setText(String.valueOf(nonFollowers.size()));
                    MainActivity.fansCount.setText(String.valueOf(fans.size()));
                    MainActivity.mutualCount.setText(String.valueOf(common.size()));
                    System.out.println(fans.size());

                }
                else
                {
                    MainActivity.nonFollowersCount.setText(String.valueOf(0));
                    MainActivity.fansCount.setText(String.valueOf(0));
                    MainActivity.mutualCount.setText(String.valueOf(0));

                }



                int[] j=getGainedLostCount();
                MainActivity.tvFollowersLostCount.setText(String.valueOf(j[1]));
                MainActivity.tvFollowersGainedCount.setText(String.valueOf(j[0]));
                dialog.dismiss();
            }
        });
    }
    public ArrayList<TrackInfo> getNonFollowers() throws JSONException {
        for(int j=0;j<followsListArray.length();j++)
        {
            JSONObject iteratorObject = followsListArray.getJSONObject(j);
            for (int i = 0; i < nonFollowers.size(); i++) {



                if(iteratorObject.getString("username").equals(nonFollowers.get(i)))

                    arrayListNonFollowers.add(new TrackInfo(iteratorObject.getString("username"),iteratorObject.getString("profile_picture")));

            }}
        return arrayListNonFollowers;

    }
    public ArrayList<TrackInfo> getFans() throws JSONException {
        for(int j=0;j<followedByListArray.length();j++)
        {  JSONObject iteratorObject = followedByListArray.getJSONObject(j);
            for (int i = 0; i < fans.size(); i++) {

                if (iteratorObject.getString("username").equals(fans.get(i)))

                    arrayListFans.add(new TrackInfo(iteratorObject.getString("username"), iteratorObject.getString("profile_picture")));

            }     }
        return arrayListFans;

    }
    public ArrayList<TrackInfo> getMutual() throws JSONException {
        for(int j=0;j<followsListArray.length();j++)
        {  JSONObject iteratorObject = followsListArray.getJSONObject(j);
            for (int i = 0; i < common.size(); i++) {

                if(iteratorObject.getString("username").equals(common.get(i)))

                    arrayListMutual.add(new TrackInfo(iteratorObject.getString("username"),iteratorObject.getString("profile_picture")));

            }}
        return arrayListMutual;

    }
    public void setGainedLostUsers(JSONObject followedByListObjectNew) throws JSONException {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        String strFollowedByList=sharedPreferences.getString("followedBy",null);


        if(strFollowedByList!=null&&followedByListObjectNew!=null)
        {
            JSONObject followedByListObject=new JSONObject(strFollowedByList);
            followedByListArrayOld=followedByListObject.getJSONArray("data");

            for (int i = 0; i < followedByListArrayOld.length(); i++) {
                //lost
                JSONObject iteratorObject = followedByListArrayOld.getJSONObject(i);
                followedByList.add(i,iteratorObject.getString("username"));
            }

            followedByListArrayNew=followedByListObjectNew.getJSONArray("data");

            for (int i = 0; i < followedByListArrayNew.length(); i++) {
                //gained
                JSONObject iteratorObject = followedByListArrayNew.getJSONObject(i);
                followedByListNew.add(i,iteratorObject.getString("username"));
            }
            Collection<String> listOne = new ArrayList((followedByList));
            Collection<String> listTwo = new ArrayList((followedByListNew));



            List<String> gainedList=new ArrayList<>(listTwo);
            List<String> lostList=new ArrayList<>(listOne);
            gainedList.removeAll(listOne);
            lostList.removeAll(listTwo);
            System.out.println(gainedList);
            System.out.println(lostList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // editor.remove("GAINEDUSERS").apply();
            //editor.remove("LOSTUSERS").apply();
            editor.putString("GAINEDUSERS",gainedList.toString());
            editor.putString("LOSTUSERS",lostList.toString());
            editor.apply();


        }
    }
    public int[] getGainedLostCount()
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        String strGainedList=sharedPreferences.getString("GAINEDUSERS",null);
        String strLostList=sharedPreferences.getString("LOSTUSERS",null);
        if(strGainedList!=null||strLostList!=null)
        {
            List<String> myList = new ArrayList<String>(Arrays.asList(strGainedList.split(",")));
            List<String> myList2 = new ArrayList<String>(Arrays.asList(strLostList.split(",")));
            if("[]".equals(myList.get(0))&&"[]".equals(myList2.get(0)))
            {
                return  new int[]{0,0};
            }
            else if ("[]".equals(myList.get(0))&&!("[]".equals(myList2.get(0))))
            {
                return  new int[]{0,myList2.size()};
            }
            else if(!("[]".equals(myList.get(0)))&&"[]".equals(myList2.get(0)))
            {
                return  new int[]{myList.size(),0};
            }
            else {
                return new int[]{(myList.size()), (myList2.size())};
            }}
        else
        {
            return  new int[]{0,0};
        }
    }
    public ArrayList<TrackInfo> getGainedFollowers() throws JSONException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String strGainedList = sharedPreferences.getString("GAINEDUSERS", null);
        ArrayList<TrackInfo> arrayListGained = new ArrayList<>();
        if (strGainedList != null) {
            List<String> myList = new ArrayList<String>(Arrays.asList(strGainedList.split(",")));

            for (int j = 0; j < followedByListArrayNew.length(); j++) {
                JSONObject iteratorObject = followedByListArrayNew.getJSONObject(j);
                for (int i = 0; i < myList.size(); i++) {
                    if (iteratorObject.getString("username").equals(myList.get(i).toString().replace("[", "").replace("]", "").trim()))

                        arrayListGained.add(new TrackInfo(iteratorObject.getString("username"), iteratorObject.getString("profile_picture")));

                }
            }
        }
        return arrayListGained;

    }
    public ArrayList<TrackInfo> getLostFollowers() throws JSONException {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        String strLostList=sharedPreferences.getString("LOSTUSERS",null);
        ArrayList<TrackInfo> arrayListLost=new ArrayList<>();
        System.out.println(followedByListArray);
        if(strLostList!=null) {

            List<String> myList = new ArrayList<String>(Arrays.asList(strLostList.split(",")));
            for(int j=0;j<followedByListArrayOld.length();j++)
            {
                JSONObject iteratorObject = followedByListArrayOld.getJSONObject(j);

                for (int i = 0; i < myList.size(); i++) {
                    System.out.println(myList.get(i));
                    if(iteratorObject.getString("username").equals(myList.get(i).toString().replace("[", "") .replace("]", "")  .trim()))

                        arrayListLost.add(new TrackInfo(iteratorObject.getString("username"),iteratorObject.getString("profile_picture")));

                }}}
        return arrayListLost;

    }

}
