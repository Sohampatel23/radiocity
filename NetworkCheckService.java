package com.app.radiocity.services;

import static com.app.radiocity.Utils.isInternetConnected;
import static com.app.radiocity.other.AppConstant.devFlag;
import static com.app.radiocity.other.AppConstant.internetCheckFlag;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;


public class
NetworkCheckService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showToast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void showToast() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initBroadcast(isInternetConnected(getApplicationContext()));
                showToast();
            }
        }, 10000);
    }

    private void initBroadcast(boolean b) {
        Intent intent = new Intent(devFlag);
        intent.putExtra(internetCheckFlag, b);
        sendBroadcast(intent);
    }
}


