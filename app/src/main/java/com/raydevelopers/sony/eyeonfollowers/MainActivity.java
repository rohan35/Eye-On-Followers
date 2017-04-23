package com.raydevelopers.sony.eyeonfollowers;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raydevelopers.sony.eyeonfollowers.fetchers.GetUserData;
import com.raydevelopers.sony.eyeonfollowers.fetchers.OnUserDataReceived;
import com.raydevelopers.sony.eyeonfollowers.models.TrackInfo;
import com.raydevelopers.sony.eyeonfollowers.utils.InflateRecyclerViewActivity;
import com.raydevelopers.sony.eyeonfollowers.utils.MainRecyclerViewAdapter;
import com.raydevelopers.sony.eyeonfollowers.utils.NetworkUtils;
import com.raydevelopers.sony.eyeonfollowers.utils.SetTrackingDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.pb)
    ProgressBar pb;
    public static TextView tvFollowersLostCount;
    public static TextView tvFollowersGainedCount;
    public static  TextView nonFollowersCount;
    public static TextView fansCount;
    public static AlertDialog dialog ;
    public static TextView mutualCount;
    private ImageView userImage;
    private RecyclerView mRecyclerView;
    private MainRecyclerViewAdapter mAdapter;
    private Toolbar toolbar;
    private ArrayList<TrackInfo> arrayList=new ArrayList<>();
    JSONObject jsonData,userData ;

    TextView followedBy,follows,media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        userImage=(ImageView)findViewById(R.id.user_image);
        followedBy=(TextView)findViewById(R.id.followed_by);
        follows=(TextView)findViewById(R.id.follows);
        media=(TextView)findViewById(R.id.media);
        mutualCount=(TextView)findViewById(R.id.mutual_count);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        dialog= new SpotsDialog(MainActivity.this);
        tryAgain();

        tvFollowersGainedCount=(TextView)findViewById(R.id.gained_count);
        tvFollowersLostCount=(TextView)findViewById(R.id.lost_count);
        nonFollowersCount=(TextView)findViewById(R.id.non_followers_count);
        fansCount=(TextView)findViewById(R.id.fans_count);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                tryAgain();
            }
        });



    }
    public void tryAgain()
    {
        if(NetworkUtils.isNetworkConnected(this))
        {
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            String firstCheck=sharedPreferences.getString("FIRST",null);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if(firstCheck==null)
            {
                editor.putString("FIRST","1");
                editor.apply();
                getUserData().fetchdata();
                tryAgain();

            }
            else
            {
                getUserData().fetchdata();
            }
        }
        else {
            Snackbar.make(findViewById(R.id.cl), "No Internet", Snackbar.LENGTH_LONG).show();
        }
    }
    public GetUserData getUserData() {
        return new GetUserData(this,new OnUserDataReceived() {
            //Override All the methods of the interface
            @Override
            public void preRetreiving() {
                dialog.show();

            }

            @Override
            public void onDataRetreived(JSONObject jsonObject) {
                try {


                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String followedByCountOld = sharedPreferences.getString("userData",null);//second parameter is necessary ie.,Value to return if this preference does not exist.


                    String username=jsonObject.getString("username");
                    String imagePath=jsonObject.getString("profile_picture");
                    JSONObject counts=jsonObject.getJSONObject("counts");
                    String mediaCount=counts.getString("media");
                    String followsCount=counts.getString("follows");
                    String followedByCount=counts.getString("followed_by");
                    SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                    SharedPreferences.Editor editor=sharedPref.edit();

                    Picasso.with(getApplicationContext()).load(imagePath).into(userImage);
                    toolbar.setTitle(username);
                    media.setText(mediaCount);
                    followedBy.setText(followedByCount);
                    follows.setText(followsCount);
                    new SetTrackingDetails(MainActivity.this).getTrackCount();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }



        });

    }
    public void getGainedFollowers(View v) throws JSONException {
        buttonClickAnim(v);

        arrayList=new SetTrackingDetails(this).getGainedFollowers();
        sendIntentToInflate();
    }

    public void getLostFollowers(View v) throws JSONException {
        buttonClickAnim(v);

        arrayList=new SetTrackingDetails(this).getLostFollowers();
        sendIntentToInflate();
    }
    public void getNonFollowers(View v) throws JSONException {
        buttonClickAnim(v);

        arrayList=new SetTrackingDetails(this).getNonFollowers();
        sendIntentToInflate();
    }
    public void getMutual(View v) throws JSONException {
        buttonClickAnim(v);

        arrayList=new SetTrackingDetails(this).getMutual();
        sendIntentToInflate();
    }
    public void getFans(View v) throws JSONException {
        buttonClickAnim(v);

        arrayList=new SetTrackingDetails(this).getFans();
        sendIntentToInflate();
    }

    public void sendIntentToInflate()
    {
        Intent intent = new Intent(MainActivity.this, InflateRecyclerViewActivity.class);
        intent.putExtra("FILES_TO_SEND", arrayList);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void buttonClickAnim(View v)
    {
        int finalRadius=(int)Math.hypot(v.getWidth()/2,v.getHeight()/2);
        Animator anim= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(v,v.getWidth()/2,v.getHeight()/2,0,finalRadius);
            anim.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
