package com.app.radiocity.ViewModel;

import static com.app.radiocity.other.SharedConst.LOGINID;
import static com.app.radiocity.other.SharedConst.TOKEN;
import static com.app.radiocity.other.SharedConst.USERID;
import static com.app.radiocity.other.SharedConst.USERNAME;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.radiocity.R;


import com.app.radiocity.Repository.MobileLogoutRepository;
import com.app.radiocity.View.ProfileDetail;

import com.app.radiocity.databinding.ActivityProfileBinding;
import com.app.radiocity.model.LogoutModel;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;


import java.util.List;

public class


ProfileViewModel extends ViewModel implements View.OnClickListener {
    private Activity activity;
    private ActivityProfileBinding binding;

    MobileLogoutRepository logoutRepository;
    DialogUtils utils;

    public <T> ProfileViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityProfileBinding) binding;
        logoutRepository = new MobileLogoutRepository(activity);
        utils = new DialogUtils(activity);
        initview();
    }

    public MutableLiveData<List<LogoutModel>> userlogout(String loginid,String token) {
        return logoutRepository.userlogout(loginid,token);
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding.setIsLoading(true);
        binding.appBar.appBarTitle.setText(R.string.prohead);


        binding.appBar.backIconImageButton.setOnClickListener(this);
        binding.layoutLogoutButton.setOnClickListener(this);

      binding.setUserData(SharedPrefUtils.getUserDetails(activity));

      binding.setIsLoading(false);
//        binding.emailid.setText("sohampatel@gmail.com");
//        binding.location.setText("vadodara");
//        binding.mobileno.setText("9955221147");
//        binding.username.setText("Soham");
       // setProfileImage();
    }

    private void setProfileImage() {
      //  String senderFirstLetter = SharedPrefUtils.getStringUtils(activity,USERNAME).subSequence(0, 1).toString();
        binding.tvProfilecharacter.setText("T");
        binding.setIsLoading(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIconImageButton:
                performBackPreseed();
                break;
            case R.id.layout_logoutButton:
                performLogout();
                break;

        }
    }

    private void moveToDetail() {
        Intent im = new Intent(activity, ProfileDetail.class);
        activity.startActivity(im);
    }

    private void performBackPreseed() {
        activity.onBackPressed();
    }

    private void performLogout() {
        String loginid = SharedPrefUtils.getStringUtils(activity, LOGINID);
        String token = SharedPrefUtils.getStringUtils(activity, TOKEN);
        utils.dialogToLogout(loginid,token);
    }

}
