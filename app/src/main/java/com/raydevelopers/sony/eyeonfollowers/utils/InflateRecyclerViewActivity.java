package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.raydevelopers.sony.eyeonfollowers.R;
import com.raydevelopers.sony.eyeonfollowers.models.TrackInfo;

import java.util.ArrayList;

/**
 * Created by SONY on 23-04-2017.
 */

public class InflateRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TextView mNoData;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracked_recycler_view);
        ArrayList<TrackInfo> arrayList =  (ArrayList<TrackInfo>)getIntent().getSerializableExtra("FILES_TO_SEND");
        mRecyclerView=(RecyclerView)findViewById(R.id.tracked_recycler_view);
        mNoData=(TextView)findViewById(R.id.no_data);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.app_name));
        if(arrayList.isEmpty())
        {
            mRecyclerView.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }
        else
        {
            mNoData.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            MainRecyclerViewAdapter adapter=new MainRecyclerViewAdapter(this,arrayList);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(adapter);
        }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.remove(getString(R.string.access_token)).commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        }
        else  if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
