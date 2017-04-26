package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.raydevelopers.sony.eyeonfollowers.MainActivity;
import com.raydevelopers.sony.eyeonfollowers.R;

/**
 * Created by SONY on 23-04-2017.
 */


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken=sharedPreferences.getString(getString(R.string.access_token),null);
        if(accessToken!=null)
        {
            Intent i=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void getToken(View v)
    {
        InstagramLoginDialog x = new InstagramLoginDialog();
        x.show(getSupportFragmentManager(), "instagram_login_fragment");

    }
    public void getExpiredToken()
    {
    InstagramLoginDialog x = new InstagramLoginDialog();
    x.show(getSupportFragmentManager(), "instagram_login_fragment");}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}