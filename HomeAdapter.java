package com.app.radiocity.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.Provider.DrawerProvider;
import com.app.radiocity.Provider.HomeProvider;
import com.app.radiocity.R;
import com.app.radiocity.Utils;
import com.app.radiocity.View.AddCAF;
import com.app.radiocity.View.CreateCAFActivity;
import com.app.radiocity.View.ProfileActivity;
import com.app.radiocity.View.QueryActivity;
import com.app.radiocity.databinding.MenuBinding;
import com.app.radiocity.databinding.MenuListBinding;
import com.app.radiocity.databinding.QueriesListBinding;
import com.app.radiocity.model.GetMenuModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MenuViewHolder> {
    private Context context;
    Activity activity;
    ArrayList<GetMenuModel> homeProviders;

    String type;
    DialogUtils utils;

    public HomeAdapter(ArrayList<GetMenuModel> getDataAdapter, Context context, Activity activity){

        this.context = context;
        this.homeProviders = getDataAdapter;
        this.activity = activity;
        utils = new DialogUtils(activity);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.item_list_home,parent,false);
//        return new DrawerViewHolder(view);

        MenuListBinding binding = MenuListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
//        final HomeProvider getDataAdapter1 = homeProviders.get(position);

        GetMenuModel model = homeProviders.get(position);
        holder.bind(model);
        holder.setData(model,position);
    }

    @Override
    public int getItemCount() {
        return homeProviders.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout homeclick;
        MenuListBinding menuBinding;
        public MenuViewHolder(@NonNull MenuListBinding menuBinding) {
            super(menuBinding.getRoot());
//            title = (TextView) row.findViewById(R.id.hometitles);
//            homeclick = (LinearLayout) row.findViewById(R.id.clickhome);

            this.menuBinding = menuBinding;

        }
        public void bind(GetMenuModel modal) {
            menuBinding.setValues(modal);
            menuBinding.executePendingBindings();
        }

        public void setData(GetMenuModel model, int position) {

           menuBinding.hometitles.setText(model.Screen_Name);
            String title = model.Screen_Name;
            Log.e("name",title);
            Log.e("name",title.toLowerCase());
            int menuid  = model.Menu_ID;
            if(title.toLowerCase().equals("createcaf"))
            {
                menuBinding.clickhome.setOnClickListener(view -> {
                    Intent im = new Intent(activity, CreateCAFActivity.class);
                    activity.startActivity(im);
                });
            }

            if(title.toLowerCase().equals("approvecaf"))
            {
                menuBinding.clickhome.setOnClickListener(view -> {
                    Intent im = new Intent(activity, QueryActivity.class);
                    im.putExtra("pagetitle",title);
                    activity.startActivity(im);
                });
            }
        }
    }
}
