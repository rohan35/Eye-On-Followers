package com.raydevelopers.sony.eyeonfollowers.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raydevelopers.sony.eyeonfollowers.R;
import com.raydevelopers.sony.eyeonfollowers.models.TrackInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONY on 23-04-2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<TrackInfo> arrayList=new ArrayList<>();

    public MainRecyclerViewAdapter(Context context,ArrayList<TrackInfo> arrayList1)
    {
        this.mContext=context;
        this.arrayList=arrayList1;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_recyclerview,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String username=arrayList.get(position).getmUsername();
        holder.username.setText(arrayList.get(position).getmUsername());
        Picasso.with(mContext).load(arrayList.get(position).getmProfile_pic()).into(holder.profilePic);
        holder.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/"+username);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    mContext.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/"+username)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList.size()==0)
        {
            return 0;
        }
        else
            return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView username;
        Button account;
        public MyViewHolder(View itemView) {
            super(itemView);
            profilePic=(ImageView)itemView.findViewById(R.id.profile_picture);
            username=(TextView)itemView.findViewById(R.id.username);
            account=(Button) itemView.findViewById(R.id.account);


        }
    }
}

