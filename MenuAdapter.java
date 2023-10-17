package com.app.radiocity.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.R;
import com.app.radiocity.View.CreateCAFActivity;
import com.app.radiocity.View.ProfileActivity;
import com.app.radiocity.View.ProfileDetail;
import com.app.radiocity.databinding.MenuBinding;
import com.app.radiocity.databinding.QueriesListBinding;
import com.app.radiocity.model.GetMenuModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.preferences.DialogUtils;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.menuviewholder>{
    private Context context;
    ArrayList<GetMenuModel> menuModels;
    Activity activity;
    DialogUtils utils ;
   public Boolean check = null;
   onMenuitemClick onMenuitemClick;
    public Boolean isFirst = false ;

   int count = 0;
   int selected = -1;


    public MenuAdapter(ArrayList<GetMenuModel> getDataAdapter, Context context, Activity activity, onMenuitemClick onMenuitemClick)
    {
        this.menuModels = getDataAdapter;
        this.onMenuitemClick =onMenuitemClick;
        this.context = context;
        this.activity = activity;
        utils = new DialogUtils(activity);
    }

    @NonNull
    @Override
    public menuviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuBinding binding = MenuBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new menuviewholder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull menuviewholder holder, @SuppressLint("RecyclerView") int position) {
        GetMenuModel model = menuModels.get(position);
        holder.bind(model);
        holder.setData(model,onMenuitemClick,position);
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }


    public void onBoxClick(Boolean check){
        this.check = check;
        notifyDataSetChanged();
    }

    public class menuviewholder extends RecyclerView.ViewHolder {

         MenuBinding menuBinding;
        public menuviewholder(@NonNull MenuBinding queriesListBinding) {
            super(queriesListBinding.getRoot());
            this.menuBinding = queriesListBinding;
        }
        public void bind(GetMenuModel modal) {
            menuBinding.setValues(modal);
            menuBinding.executePendingBindings();
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void setData(GetMenuModel model, onMenuitemClick onMenuitemClick, int position){



            if(position==0)
            {
                menuBinding.onceimg.setVisibility(View.VISIBLE);
                menuBinding.icondr.setImageDrawable(activity.getDrawable(R.drawable.newhomeicon));

                menuBinding.clickmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onMenuitemClick.onMenuitemClick();
                        Intent im = new Intent(context, ProfileActivity.class);
                        context.startActivity(im);

                    }
                });
            }
//
//
            else
            {
                menuBinding.onceimg.setVisibility(View.GONE);
                menuBinding.icondr.setImageDrawable(activity.getDrawable(R.drawable.newquryicon));
                menuBinding.clickmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onMenuitemClick.onMenuitemClick();
                        Intent im = new Intent(context, CreateCAFActivity.class);
                        context.startActivity(im);

                    }
                });
            }
//
//            if(position == 3)
//            {
//                menuBinding.onceimg.setVisibility(View.VISIBLE);
//                menuBinding.icondr.setImageDrawable(activity.getDrawable(R.drawable.newquryicon));
//                menuBinding.clickmenu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        onMenuitemClick.onMenuitemClick();
//                        Intent im = new Intent(context, CreateCAFActivity.class);
//                        context.startActivity(im);
//
//                    }
//                });
//            }
        }
    }

    public interface onMenuitemClick{
        void onMenuitemClick();
    }

}
