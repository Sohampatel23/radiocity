package com.app.radiocity.ViewModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.Adapter.DrawerAdapter;
import com.app.radiocity.Adapter.MenuAdapter;
import com.app.radiocity.Adapter.QueryAdapter;

import com.app.radiocity.Provider.DrawerProvider;
import com.app.radiocity.R;
import com.app.radiocity.Repository.AppVersionRepository;
import com.app.radiocity.Repository.ApproveCAFRepository;
import com.app.radiocity.Repository.GetMenuRepository;
import com.app.radiocity.Repository.PendingCafRepository;
import com.app.radiocity.View.ProfileActivity;
import com.app.radiocity.databinding.ActivityQueryBinding;
import com.app.radiocity.model.AppVersionModel;
import com.app.radiocity.model.ApproveCAFModel;
import com.app.radiocity.model.GetMenuModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class QueryViewModel extends ViewModel implements View.OnClickListener, QueryAdapter.onCheckChangeListner, MenuAdapter.onMenuitemClick{

    private Activity activity;
    private ActivityQueryBinding binding;
    boolean check = false;
    boolean appupd;
    DialogUtils utils ;
    NavigableMap<String, String> hashMap = new TreeMap<>();
    ArrayList<PendingCAFModel> QueryList = new ArrayList<PendingCAFModel>();
    QueryAdapter adapter;
    ArrayList<DrawerProvider> MenuItems = new ArrayList<>();
    ArrayList<GetMenuModel> MenuItem = new ArrayList<GetMenuModel>();
    DrawerAdapter adapter1;
    MenuAdapter menuAdapter;
    String loginid,token,versionCode;
    PendingCafRepository pendingCafRepository;
    ApproveCAFRepository approveCAFRepository;
    AppVersionRepository appVersionRepository;
    GetMenuRepository getMenuRepository;
    Dialog bottomSheetDialog;
    PackageInfo info;

    public <T> QueryViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityQueryBinding) binding;
        utils = new DialogUtils(activity);
        pendingCafRepository = new PendingCafRepository(activity);
        approveCAFRepository = new ApproveCAFRepository(activity);
        appVersionRepository = new AppVersionRepository(activity);
        getMenuRepository = new GetMenuRepository(activity);
        initview();
    }

    public MutableLiveData<List<PendingCAFModel>> getcafs(String loginid, String Token) {
        return pendingCafRepository.getpendingcafs(loginid,Token);
    }

    public MutableLiveData<List<GetMenuModel>> getmenuitems(String loginid, String Token) {
        return getMenuRepository.getmenudetails(loginid,Token);
    }

    public MutableLiveData<List<AppVersionModel>> appversion(String loginid, String Token) {
        return appVersionRepository.appversion(loginid,Token);
    }

    public MutableLiveData<List<ApproveCAFModel>> approvecafs(String loginid, String historyid,String customerid, int approved, String apexbus, String apamount, String apdays, String remarks, String Token) {
        return approveCAFRepository.approveCAF(loginid,historyid,customerid,approved,apexbus,apamount,apdays,remarks,Token);
    }

    private void initview() {

        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        utils.showprogress(activity);
        loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);

        appupd = SharedPrefUtils.getBooleanUtils(activity,"appupdate");


        binding.queryBar.backicon.setImageDrawable(activity.getDrawable(R.drawable.ic_baseline_keyboard_backspace_black));
        ViewCompat.setNestedScrollingEnabled(binding.recyclerQuery,false);
        binding.recyclerQuery.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.recyclerQuery.setLayoutManager(layoutManager);

         getpendingcafs();

         if(!appupd)
         { AppVersion();}

        binding.agentqueryTitle.profilelayout.setVisibility(View.VISIBLE);
        binding.agentqueryTitle.checkall.setOnClickListener(this);
       binding.agentqueryTitle.cancelquerybtn.setOnClickListener(this);
       binding.agentqueryTitle.acceptquerybtn.setOnClickListener(this);
       binding.queryBar.backicon.setOnClickListener(this);

        binding.navListquery.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.navListquery.setLayoutManager(layoutManager1);
       GetMenu();
        menuAdapter = new MenuAdapter(MenuItem, activity, activity, new MenuAdapter.onMenuitemClick() {
            @Override
            public void onMenuitemClick() {
                binding.drawerQuerymain.closeDrawer(Gravity.LEFT);
            }
        });
        binding.navListquery.scrollToPosition(0);
        binding.navListquery.setAdapter(adapter1);
//
//        String title1 = "Home";
//        int t1 = R.drawable.newhomeicon;
//
//        String title2 = "Create CAF";
//        int t2 =R.drawable.newquryicon;
//
//        String title3 = "My Profile";
//        int t3 = R.drawable.profileicon;
//
//        String title4 = "Log Out";
//        int t4 = R.drawable.logblack;
//
//        ViewCompat.setNestedScrollingEnabled(binding.navListquery,false);
//        binding.navListquery.setHasFixedSize(false);
//        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

////        MenuItems.add(new DrawerProvider(title1,t1));
//
//        MenuItems.add(new DrawerProvider(title3,t3));
//        MenuItems.add(new DrawerProvider(title2,t2));
//        MenuItems.add(new DrawerProvider(title4,t4));
//
//        adapter1 = new DrawerAdapter(MenuItems, activity, new DrawerAdapter.onDraweritemClick() {
//            @Override
//            public void onDrawerItemClick() {
//                binding.drawerQuerymain.closeDrawer(Gravity.LEFT);
//            }
//        },activity);
//
//        binding.navListquery.scrollToPosition(0);
//        binding.navListquery.setLayoutManager(layoutManager1);
//        binding.navListquery.setAdapter(adapter1);

        Intent imn = activity.getIntent();
        String title = imn.getStringExtra("pagetitle");
        binding.queryBar.tvTitle.setText(title);
        String senderFirstLetter = SharedPrefUtils.getStringUtils(activity, SharedConst.USERNAME).subSequence(0, 1).toString();
        setProfileImage(senderFirstLetter);
        binding.queryBar.profieText.setVisibility(View.GONE);
        binding.agentqueryTitle.backicon.setVisibility(View.GONE);
        binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 0.3);
        binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 0.3);
        binding.agentqueryTitle.acceptquerybtn.setClickable(false);
        binding.agentqueryTitle.cancelquerybtn.setClickable(false);

        binding.queryBar.profieText.setOnClickListener(view ->{
            Intent im = new Intent(activity, ProfileActivity.class);
            activity.startActivity(im);
        });
    }

    private void setProfileImage(String senderFirstLetter) {
        binding.queryBar.profieText.setText(senderFirstLetter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.checkall:
                checkForTick();
                break;
            case R.id.acceptquerybtn:
                binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 0.3);
                binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 0.3);
                binding.agentqueryTitle.acceptquerybtn.setClickable(false);
                binding.agentqueryTitle.cancelquerybtn.setClickable(false);
                adapterCheck(1,check);
                break;
            case R.id.cancelquerybtn:
                binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 0.3);
                binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 0.3);
                binding.agentqueryTitle.acceptquerybtn.setClickable(false);
                binding.agentqueryTitle.cancelquerybtn.setClickable(false);
                adapterCheck(2,check);
                break;
            case R.id.backicon:
               // openMenu();
                activity.finish();
                break;
        }
    }

    private void openMenu() {
        binding.drawerQuerymain.openDrawer(Gravity.LEFT);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void adapterCheck(int value,Boolean chk) {

        if (chk.equals(true)) {
            bottomSheetDialog = new Dialog(activity);
            bottomSheetDialog.setContentView(R.layout.dialog_popup_remarks);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            TextView title = bottomSheetDialog.findViewById(R.id.tv_queryno);
            title.setText("All Queries");
            bottomSheetDialog.show();

            bottomSheetDialog.findViewById(R.id.btn_popup_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText remark = bottomSheetDialog.findViewById(R.id.edit_remarktext);
                    String r1 = remark.getText().toString();
                    bottomSheetDialog.dismiss();
                    for (int i = 0; i <= QueryList.size(); i++) {
                        acceptRejectCall(loginid, QueryList.get(i).HistoryID, QueryList.get(i).AgencyID, value, QueryList.get(i).Approved_ExpectedBussines, QueryList.get(i).Approved_Credit_Amount, QueryList.get(i).Approved_Credit_Days, r1, token, false);
                        if (i == QueryList.size() - 1) {
                            acceptRejectCall(loginid, QueryList.get(i).HistoryID, QueryList.get(i).AgencyID, value, QueryList.get(i).Approved_ExpectedBussines, QueryList.get(i).Approved_Credit_Amount, QueryList.get(i).Approved_Credit_Days, r1, token, true);
                        }
                    }
                }
            });

        } else {
            if (hashMap.isEmpty()) {
                Toast.makeText(activity, "Please select any of queries!", Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            } else {

                bottomSheetDialog = new Dialog(activity);
                bottomSheetDialog.setContentView(R.layout.dialog_popup_remarks);
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomSheetDialog.show();

                bottomSheetDialog.findViewById(R.id.btn_popup_send).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                            if (!entry.equals(null)) {
                                EditText remark = bottomSheetDialog.findViewById(R.id.edit_remarktext);
                                String r1 = remark.getText().toString();
                                bottomSheetDialog.dismiss();
                                try {
                                    for (int i = 0; i <= QueryList.size(); i++) {
                                        if (entry.getValue().equals(QueryList.get(i).HistoryID)) {
                                            if (hashMap.lastEntry().getValue().equals(QueryList.get(i).HistoryID)) {
                                                acceptRejectCall(loginid, QueryList.get(i).HistoryID, QueryList.get(i).AgencyID, value, QueryList.get(i).Approved_ExpectedBussines, QueryList.get(i).Approved_Credit_Amount, QueryList.get(i).Approved_Credit_Days, r1, token, true);
                                            }
                                            acceptRejectCall(loginid, QueryList.get(i).HistoryID, QueryList.get(i).AgencyID, value, QueryList.get(i).Approved_ExpectedBussines, QueryList.get(i).Approved_Credit_Amount, QueryList.get(i).Approved_Credit_Days, r1, token, false);
                                        }
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkForTick() {

        if(binding.agentqueryTitle.checkall.isChecked())
        {
            check = true;
            adapter.onBoxClick(check);
            binding.agentqueryTitle.checkall.setButtonTintList(ColorStateList.valueOf(activity.getColor(R.color.checkbox1)));
            binding.agentqueryTitle.tvTitle.setText("Deselect All");
            binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 1);
            binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 1);
            binding.agentqueryTitle.acceptquerybtn.setClickable(true);
            binding.agentqueryTitle.cancelquerybtn.setClickable(true);
        }
        else
        {
            check = false;
            adapter.onBoxClick(check);
            binding.agentqueryTitle.checkall.setButtonTintList(ColorStateList.valueOf(activity.getColor(R.color.checkbox0)));
            binding.agentqueryTitle.tvTitle.setText("Select All");
            binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 0.3);
            binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 0.3);
            binding.agentqueryTitle.acceptquerybtn.setClickable(false);
            binding.agentqueryTitle.cancelquerybtn.setClickable(false);
        }
    }

    private ArrayList<PendingCAFModel> getpendingcafs()
    {

        getcafs(loginid,token).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
                    hashMap.clear();
                    QueryList.clear();

                    QueryList.addAll(data);
                    adapter = new QueryAdapter(QueryList, activity, activity,this);
                    binding.recyclerQuery.scrollToPosition(0);
                    binding.recyclerQuery.setAdapter(adapter);
                    utils.hideprogress(activity);
            }
            else
            {
                Toast.makeText(activity, "No Records Found", Toast.LENGTH_SHORT).show();
                utils.hideprogress(activity);
            }
        });
        return QueryList;
    }

    public void acceptRejectCall(String loginid, String historyid, String customerid, int approved, String apexbus, String apamount, String apdays, String remarks, String Token, Boolean isLast)
    {
        approvecafs(loginid,historyid,customerid,approved,apexbus,apamount,apdays,remarks,Token).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
                if(isLast)
                {
                    String status = data.get(0).Status;
                    Toast.makeText(activity, status, Toast.LENGTH_SHORT).show();
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition(0, 0);
                    binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 1);
                    binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 1);
                    binding.agentqueryTitle.acceptquerybtn.setClickable(true);
                    binding.agentqueryTitle.cancelquerybtn.setClickable(true);
                }
                else
                {
                    String status = data.get(0).Status;
                    Toast.makeText(activity, status, Toast.LENGTH_SHORT).show();
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition(0, 0);
                    binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 1);
                    binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 1);
                    binding.agentqueryTitle.acceptquerybtn.setClickable(true);
                    binding.agentqueryTitle.cancelquerybtn.setClickable(true);
                }
            }
        });
    }

    public void AppVersion()
    {
        appversion(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                String param = data.get(0).ParameterValue;
                String version = data.get(0).ParameterCode;
                PackageManager manager = activity.getPackageManager();
                try {
                     info = manager.getPackageInfo(activity.getPackageName(), 0
                     );
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                versionCode = info.versionName;
                if(!Objects.equals(versionCode,param))
                {
                    utils.hideprogress(activity);
                    openDialog();
                }
            }
        });
    }

    @Override
    public void onCheckChange(boolean b, int position, String historyid, String customerid, String ApExBus, String Apamount, String Apdays, int count) {
        QueryList.get(position).isChecked = b;

        if (count > 0){
            binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 1);
            binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 1);
            binding.agentqueryTitle.acceptquerybtn.setClickable(true);
            binding.agentqueryTitle.cancelquerybtn.setClickable(true);
        }else {
            binding.agentqueryTitle.acceptquerybtn.setAlpha((float) 0.3);
            binding.agentqueryTitle.cancelquerybtn.setAlpha((float) 0.3);
            binding.agentqueryTitle.acceptquerybtn.setClickable(false);
            binding.agentqueryTitle.cancelquerybtn.setClickable(false);
        }
        if (b)
        {
            hashMap.put(historyid,QueryList.get(position).HistoryID);
        }
        else
        {
            hashMap.remove(historyid);
        }
        adapter.notifyDataSetChanged();
    }

    private void openDialog()
    {
        LayoutInflater inflater = (LayoutInflater)  activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.dialog_popup_appversion, null);

        int width1 = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width1, height, focusable);

        popupWindow.showAtLocation(activity.findViewById(R.id.drawer_querymain), Gravity.BOTTOM, 0, 0);
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager manager = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) container.getLayoutParams();
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.3f;
        manager.updateViewLayout(container, params);

        popupView.findViewById(R.id.btn_version_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                SharedPrefUtils.saveBoolean(activity,"appupdate",true);
            }
        });

        popupView.findViewById(R.id.btn_version_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.app.radiocity"));
                   activity.startActivity(viewIntent);
                }catch(Exception e) {
                    Toast.makeText(activity,"Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void GetMenu()
    {
        getmenuitems(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                    MenuItem.clear();
                    MenuItem.addAll(data);
                   GetMenuModel m = new GetMenuModel();
                menuAdapter = new MenuAdapter(MenuItem, activity, activity,this);
                binding.navListquery.scrollToPosition(0);
                binding.navListquery.setAdapter(menuAdapter);
            }
        });
    }

    @Override
    public void onMenuitemClick() {
        binding.drawerQuerymain.closeDrawer(Gravity.LEFT);
    }

}
