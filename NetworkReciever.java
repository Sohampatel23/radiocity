package com.app.radiocity.network;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.app.radiocity.R;


public class NetworkReciever extends BroadcastReceiver {
    Activity activity;
    LinearLayout linearLayout;
    public NetworkReciever(Activity activity,LinearLayout linearLayout){
        this.activity =activity;
        this.linearLayout = linearLayout;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!NetCheck.isConnectedToInternet(activity)){

            LayoutInflater inflater = (LayoutInflater)  activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.internet_error_layout, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
            View container = (View) popupWindow.getContentView().getParent();
            WindowManager manager = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();
            params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.3f;
            manager.updateViewLayout(container, params);
        }
    }

}
