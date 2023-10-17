package com.app.radiocity.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.app.radiocity.View.HomeActivity;
import com.app.radiocity.View.LoginScreenActivity;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.app.radiocity.databinding.ActivitySplashBinding;

public class MainViewModel extends ViewModel {

    private Activity activity;
    private ActivitySplashBinding binding;

    public <T> MainViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivitySplashBinding) binding;
        initViews();
    }

    private void initViews() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPrefUtils.getBooleanUtils(activity, SharedConst.ISLOGIN)){

                    Intent intent = new Intent(activity, HomeActivity.class);//CAFscreen
                    SharedPrefUtils.saveBoolean(activity,"appupdate",false);
                        activity.startActivity(intent);
                        activity.finish();

                }else{
                    Intent im = new Intent(activity, LoginScreenActivity.class);//loginscreen
                    im.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    im.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    im.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(im);
                }
            }
        },3000);
    }
}
