package com.app.radiocity.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.Adapter.HomeAdapter;
import com.app.radiocity.Adapter.QueryAdapter;
import com.app.radiocity.Provider.DrawerProvider;
import com.app.radiocity.Provider.HomeProvider;
import com.app.radiocity.R;

import com.app.radiocity.Repository.CreateCAFRepository;
import com.app.radiocity.Repository.FileDownloadRepository;
import com.app.radiocity.Repository.GetMenuRepository;
import com.app.radiocity.Repository.PendingCafRepository;
import com.app.radiocity.View.CreateCAFActivity;
import com.app.radiocity.View.ProfileActivity;
import com.app.radiocity.View.QueryActivity;
import com.app.radiocity.databinding.ActivityHomeBinding;
import com.app.radiocity.model.FileDownloadModel;
import com.app.radiocity.model.GetMenuModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.other.DialogUtil;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements View.OnClickListener{
    Activity activity;
    ActivityHomeBinding binding;
    HomeAdapter adapter;
    DialogUtils utils;
    String loginid,token;
    ArrayList<HomeProvider> homeProviders = new ArrayList<>();
    ArrayList<GetMenuModel> getMenuModels = new ArrayList<>();
    GetMenuRepository getMenuRepository;
    FileDownloadRepository fileDownloadRepository;

    public <T> HomeViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityHomeBinding) binding;
        utils = new DialogUtils(activity);
        getMenuRepository = new GetMenuRepository(activity);
        fileDownloadRepository = new FileDownloadRepository(activity);
        initview();
    }

    public MutableLiveData<List<GetMenuModel>> getmenu(String loginid, String Token) {
        return getMenuRepository.getmenudetails(loginid,Token);
    }

    public MutableLiveData<List<FileDownloadModel>> filelink(String loginid, String Token) {
        return fileDownloadRepository.filedownload(loginid,Token);
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        binding.homeTitle.backicon.setImageDrawable(activity.getDrawable(R.drawable.homelogout));
        binding.homeTitle.tvTitle.setText("HOME");
        String senderFirstLetter = SharedPrefUtils.getStringUtils(activity, SharedConst.USERNAME).subSequence(0, 1).toString();
        setProfileImage(senderFirstLetter);

        loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);

        ViewCompat.setNestedScrollingEnabled(binding.rvHomelist,false);
        binding.rvHomelist.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.rvHomelist.setLayoutManager(layoutManager1);
        getMenuItems();

        binding.homeTitle.profieText.setVisibility(View.INVISIBLE);
        binding.homeTitle.profieText.setClickable(false);
        binding.homeTitle.profieText.setEnabled(false);

        binding.homeTitle.profieText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent im = new Intent(activity, ProfileActivity.class);
                activity.startActivity(im);
            }
        });

        binding.homeTitle.backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
                String token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);
                utils.dialogToLogout(loginid,token);
            }
        });

        getfilelink();
    }

    private void setProfileImage(String senderFirstLetter) {
        binding.homeTitle.profieText.setText(senderFirstLetter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.profieText:
                moveToProfile();
                break;

            case R.id.backicon:
              logout();
                break;
        }
    }

    private void logout() {
        String loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        String token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);
        utils.dialogToLogout(loginid,token);
    }

    private void moveToProfile() {
        Intent im = new Intent(activity, ProfileActivity.class);
        activity.startActivity(im);
    }

    private void getMenuItems(){
        getmenu(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                getMenuModels.clear();
                getMenuModels.addAll(data);
                adapter = new HomeAdapter(getMenuModels,activity,activity);
                binding.rvHomelist.scrollToPosition(0);
                binding.rvHomelist.setAdapter(adapter);
            }
        });
    }

    private void getfilelink(){
        filelink(loginid,token).observe((LifecycleOwner) activity,data -> {
            if(data!=null)
            {
                String link = data.get(0).ParameterValue;
                SharedPrefUtils.saveStringToUtils(activity,SharedConst.FILELINK,link);
            }
        });
    }
}
