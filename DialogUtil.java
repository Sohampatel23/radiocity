package com.app.radiocity.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.app.radiocity.R;


public class DialogUtil {
    Activity activity;
    AlertDialog alertDialog;
    public DialogUtil(Activity activity) {
        this.activity = activity;
        initalizeDialog();
    }

    private void initalizeDialog() {
        if (activity!=null){
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
    }
   public void showNoInternetDialog(){
        if(alertDialog!=null&& !alertDialog.isShowing()){
            alertDialog.show();
        }
    }

    public void dismissInternetDialog(){
        if (alertDialog!=null&&alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

}
