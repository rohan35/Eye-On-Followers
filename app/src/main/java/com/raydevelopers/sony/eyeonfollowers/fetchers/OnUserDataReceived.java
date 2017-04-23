package com.raydevelopers.sony.eyeonfollowers.fetchers;

import org.json.JSONObject;

/**
 * Created by SONY on 23-04-2017.
 */

public interface OnUserDataReceived {
    void preRetreiving();
    void onDataRetreived(JSONObject jsonObject);
}
