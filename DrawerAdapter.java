package com.app.radiocity.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.View.CreateCAFActivity;
import com.app.radiocity.Provider.DrawerProvider;
import com.app.radiocity.Utils;
import com.app.radiocity.View.ProfileActivity;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.app.radiocity.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
    private Context context;
    Activity activity;
    ArrayList<DrawerProvider> DrawerItems;
    onDraweritemClick onDraweritemClick;
    String type;
    DialogUtils utils;

    public DrawerAdapter( ArrayList<DrawerProvider> getDataAdapter, Context context,onDraweritemClick onDraweritemClick,Activity activity){
        this.onDraweritemClick =onDraweritemClick;
        this.context = context;
        this.DrawerItems = getDataAdapter;
        this.activity = activity;
        utils = new DialogUtils(activity);
    }

    @NonNull
    @Override
    public DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.content_menu_item,parent,false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerViewHolder holder, int position) {
        final DrawerProvider getDataAdapter1 = DrawerItems.get(position);

        holder.title.setText(getDataAdapter1.getTitle());
        int path = getDataAdapter1.getImage();
        type = SharedPrefUtils.getStringUtils(context, SharedConst.AGENT);
        Glide.with(context).load(path).dontAnimate().into(holder.img);

        if(position==0)
        {
            holder.oimg.setVisibility(View.VISIBLE);
            holder.menuclc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    onDraweritemClick.onDrawerItemClick();
//                    Intent im = new Intent(context, DashboardActivity.class);
//                    context.startActivity(im);


                    onDraweritemClick.onDrawerItemClick();
                    Intent im = new Intent(context, ProfileActivity.class);
                    context.startActivity(im);

                }
            });
        }

        if(position==1)
        {
            holder.menuclc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent im = new Intent(context, QueryActivity.class);
//                    im.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    context.startActivity(im);
                    onDraweritemClick.onDrawerItemClick();
                    Intent im = new Intent(context, CreateCAFActivity.class);
                    context.startActivity(im);


                }
            });
        }

        if (position==2)
        {
            holder.menuclc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
                    String token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);
                    utils.dialogToLogout(loginid,token);

//                    if(type.equals("agent")) {
//                        onDraweritemClick.onDrawerItemClick();
//                        Intent im = new Intent(context, AgentProfile.class);
//                        context.startActivity(im);
//                    }
//                    else {
//                        onDraweritemClick.onDrawerItemClick();
//                        Intent im = new Intent(context, ProfileActivity.class);
//                        context.startActivity(im);
//                    }
                }
            });
        }

        if(position==3) {
            Typeface t =  Utils.getBold(context);
            holder.title.setTypeface(t);
            holder.title.setTextColor(activity.getResources().getColor(R.color.forgot));
            holder.menuclc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
                    String token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);
                    utils.dialogToLogout(loginid,token);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return DrawerItems.size();
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        LinearLayout oimg,menuclc;
        public DrawerViewHolder(@NonNull View row) {
            super(row);
            title = (TextView) row.findViewById(R.id.titledr);
            img = (ImageView) row.findViewById(R.id.icondr);
            oimg = (LinearLayout) row.findViewById(R.id.onceimg);
            menuclc = (LinearLayout) row.findViewById(R.id.clickmenu);
        }
    }

    public interface onDraweritemClick{
        void onDrawerItemClick();
    }
}
