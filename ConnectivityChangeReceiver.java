package com.app.radiocity.broadcast;

import static com.app.radiocity.other.AppConstant.devFlag;
import static com.app.radiocity.other.AppConstant.internetCheckFlag;
import static com.app.radiocity.other.AppConstant.internetConnectionFlag;
import static com.app.radiocity.other.AppConstant.wifiConnectionFlag;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.app.radiocity.View.BaseActivity;
import com.app.radiocity.R;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    BaseActivity activity;
    AlertDialog alertDialog;
    public ConnectivityChangeReceiver(BaseActivity baseActivity){
        this.activity=baseActivity;
        initAlertDialog();
    }

    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.internet_error_layout, null);
        builder.setView(dialogView);
        AppCompatImageView imageView = dialogView.findViewById(R.id.close_icon_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissInternetDialog();
            }
        });
        alertDialog = builder.create();
    }

    void showNoInternetDialog(){
        if(alertDialog!=null&& !alertDialog.isShowing()){
            alertDialog.show();
        }
    }

    void dismissInternetDialog(){
        if (alertDialog!=null&&alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(devFlag)){
            if(intent.hasExtra(internetCheckFlag)){
                boolean isConnected = intent.getExtras().getBoolean(internetCheckFlag);
                internetConnectionDialogInit(isConnected);
            }
        }
        if (action.equals(wifiConnectionFlag)||action.equals(internetConnectionFlag)){
            internetConnectionDialogInit(isConnected(context));
        }
    }


    private void internetConnectionDialogInit(boolean connected) {
        if (connected){
            dismissInternetDialog();
        }else{
            showNoInternetDialog();
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


}
