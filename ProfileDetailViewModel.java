package com.app.radiocity.ViewModel;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.Adapter.AadharAdapter;
import com.app.radiocity.Adapter.ChequeAdapter;
import com.app.radiocity.Adapter.FilesAdapter;
import com.app.radiocity.Adapter.GstAdapter;
import com.app.radiocity.Adapter.PanAdapter;
import com.app.radiocity.Adapter.TCAdapter;
import com.app.radiocity.Provider.AadharProvider;
import com.app.radiocity.Provider.ChequeProvider;
import com.app.radiocity.Provider.DrawerProvider;
import com.app.radiocity.Provider.FilesProvider;
import com.app.radiocity.Provider.GstProvider;
import com.app.radiocity.Provider.PanProvider;
import com.app.radiocity.Provider.TCProvider;
import com.app.radiocity.R;

import com.app.radiocity.Repository.ApproveCAFRepository;
import com.app.radiocity.Repository.CustomerRepository;
import com.app.radiocity.Repository.PendingCafRepository;
import com.app.radiocity.Repository.ViewCafRepository;
import com.app.radiocity.databinding.ActivityProfileBinding;

import com.app.radiocity.databinding.ActivityProfileDetailBinding;
import com.app.radiocity.model.ApproveCAFModel;
import com.app.radiocity.model.CustomerModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.model.ViewCAFModel;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileDetailViewModel extends ViewModel implements View.OnClickListener {
    private Activity activity;
    private ActivityProfileDetailBinding binding;
    ViewCafRepository viewCafRepository;
    PanAdapter panAdapter;
    ArrayList<PanProvider> panProviders = new ArrayList<>();
    FilesAdapter filesAdapter;
    ArrayList<FilesProvider> filesProviders = new ArrayList<>();
    GstAdapter gstAdapter;
    ArrayList<GstProvider> gstProviders = new ArrayList<>();
    ChequeAdapter chequeAdapter;
    ArrayList<ChequeProvider> chequeProviders = new ArrayList<>();
    AadharAdapter aadharAdapter;
    ArrayList<AadharProvider> aadharProviders = new ArrayList<>();
    TCAdapter tcAdapter;
    ArrayList<TCProvider> tcProviders = new ArrayList<>();
    ApproveCAFRepository approveCAFRepository;
    String loginid,customerid,name,token,historyid,apexbus,apamount,apdays,remarks,originalfile,panfile,gstfile,tcfile,aadharfile,chequefile,filelink;
    DialogUtils utils;
    Dialog dialog;

    public <T> ProfileDetailViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityProfileDetailBinding) binding;
        utils = new DialogUtils(activity);
        viewCafRepository = new ViewCafRepository(activity);
        approveCAFRepository = new ApproveCAFRepository(activity);
        initview();
    }

    public MutableLiveData<List<ViewCAFModel>> viewcafs(String loginid, String customerid, String token) {
        return viewCafRepository.getpendingcafs(loginid,customerid,token);
    }

    public MutableLiveData<List<ApproveCAFModel>> approvecafs(String loginid, String historyid, String customerid, int approved, String apexbus, String apamount, String apdays, String remarks, String Token) {
        return approveCAFRepository.approveCAF(loginid,historyid,customerid,approved,apexbus,apamount,apdays,remarks,Token);
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding.setIsLoading(true);

        binding.appBar.tvTitle.setText(R.string.details);

        binding.appBar.backicon.setOnClickListener(this);
        binding.qfooter.btnQueryAccept.setOnClickListener(this);
        binding.qfooter.btnQueryReject.setOnClickListener(this);
        binding.appBar.backicon.setImageDrawable(activity.getDrawable(R.drawable.ic_baseline_keyboard_backspace_black));

        loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);
        filelink = SharedPrefUtils.getStringUtils(activity, SharedConst.FILELINK);

        Intent im = activity.getIntent();
        customerid = im.getStringExtra("customerid");

        if ((ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
            viewcafsdetail();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backicon:
                removeimages();
                break;

            case R.id.btn_query_accept:
                approveaction(1);
                break;

            case R.id.btn_query_reject:
                approveaction(2);
                break;
        }
    }

    private void removeimages() {

        for(int i = 0; i<panProviders.size();i++)
        {
            try {
            if(panProviders.get(i).getFilename().contains(".jpg") || panProviders.get(i).getFilename().contains(".jpeg")) {
                File path1 = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file1 = new File(path1, panProviders.get(i).getFilename());
                if (file1.exists()) {
                    file1.delete();
                }
            }
            else
            {
            }
        }
            catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        }

        for(int i = 0; i<chequeProviders.size();i++)
        {
            try {
            if(chequeProviders.get(i).getFilename().contains(".jpg") || chequeProviders.get(i).getFilename().contains(".jpeg")) {
                File path1 = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file1 = new File(path1, chequeProviders.get(i).getFilename());
                if (file1.exists()) {
                    file1.delete();
                }
            }
            else
            {

            }
        }
            catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        }

        for(int i = 0; i<aadharProviders.size();i++)
        {
        try {
            if(aadharProviders.get(i).getFilename().contains(".jpg") || aadharProviders.get(i).getFilename().contains(".jpeg")) {
                File path1 = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file1 = new File(path1, aadharProviders.get(i).getFilename());
                if (file1.exists()) {
                    file1.delete();
                }
            }
            else
            {
            }
        }
            catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        }

        for(int i = 0; i<=gstProviders.size();i++)
        {
            try {
                if (gstProviders.get(i).getFilename().contains(".jpg") || gstProviders.get(i).getFilename().contains(".jpeg")) {
                    File path1 = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    File file1 = new File(path1, gstProviders.get(i).getFilename());
                    if (file1.exists()) {
                        file1.delete();
                    }
                } else {
                    break;
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }

        for(int i = 0; i<tcProviders.size();i++)
        {
        try {
            if(tcProviders.get(i).getFilename().contains(".jpg") || tcProviders.get(i).getFilename().contains(".jpeg")) {
                File path1 = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file1 = new File(path1, tcProviders.get(i).getFilename());
                if (file1.exists()) {
                    file1.delete();
                }
            }
            else
            {
            }
        }
            catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        }
        activity.finish();
    }

    private void viewcafsdetail()
    {
        viewcafs(loginid,customerid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                name = data.get(0).Customer_Name;
                String address = data.get(0).CustomerAddress;
                String contact = data.get(0).AgencyID;
                String email = data.get(0).EmailID;
                String tan = data.get(0).TANNo;
                String pan = data.get(0).PAN_No;
                String gst = data.get(0).GSTNNo;
                String pay = data.get(0).Payment_Terms_M;
                String emp = data.get(0).EmpName;
                String approver = data.get(0).Approver;
                String pcdays = data.get(0).Proposed_Credit_Days;
                String pcamount = data.get(0).Proposed_Credit_Amount;
                String acdays = data.get(0).Approved_Credit_Days;
                String acamount = data.get(0).Approved_Credit_Amount;
                String team = data.get(0).Team_Name;
                String segment = data.get(0).Segment_Name;
                String status = data.get(0).Status;
                String date = data.get(0).Created_Date;
                historyid = data.get(0).HistoryID;
                apamount = data.get(0).Approved_Credit_Amount;
                apexbus = data.get(0).Approved_ExpectedBussines;
                apdays = data.get(0).Approved_Credit_Days;
                originalfile = data.get(0).OriginalFileName;
                panfile = data.get(0).PANDoc_M;
                gstfile = data.get(0).GSTDoc_M;
                chequefile = data.get(0).ChequeDoc_M;
                tcfile = data.get(0).TCDoc_M;
                aadharfile = data.get(0).AadharDoc_M;

                if(panfile.isEmpty())
                {

                }
                else {
                    Boolean p = panfile.contains(",");
                    if (p) {
                        String[] panarr = panfile.split(",");
                        for (int i = 0; i < panarr.length; i++) {
                            if (panarr[i].contains(".jpg") || panarr[i].contains(".jpeg")) {
                                filesProviders.add(new FilesProvider(panarr[i], filelink + panarr[i],"PAN"));
                            } else {
                                filesProviders.add(new FilesProvider(panarr[i], "","PAN"));
                            }
                        }
                    } else {
                        filesProviders.add(new FilesProvider(panfile, filelink + panfile,"PAN"));
                    }
                }

                if(gstfile.isEmpty())
                {

                }
                else {
                    Boolean g = gstfile.contains(",");
                    if (g) {
                        String[] gstarr = gstfile.split(",");
                        for (int i = 0; i < gstarr.length; i++) {
                            if (gstarr[i].contains(".jpg") || gstarr[i].contains(".jpeg")) {
                                filesProviders.add(new FilesProvider(gstarr[i], filelink + gstarr[i],"GST"));

                            } else {
                                filesProviders.add(new FilesProvider(gstarr[i], "","GST"));
                            }
                        }
                    } else {
                        filesProviders.add(new FilesProvider(gstfile, filelink + gstfile,"GST"));
                    }
                }

                if(chequefile.isEmpty())
                {
                }
                else {
                    Boolean c = chequefile.contains(",");
                    if (c) {
                        String[] chequearr = chequefile.split(",");
                        for (int i = 0; i < chequearr.length; i++) {
                            if (chequearr[i].contains(".jpg") || chequearr[i].contains(".jpeg")) {
                                filesProviders.add(new FilesProvider(chequearr[i], filelink + chequearr[i],"Cheque"));
                            } else {
                                filesProviders.add(new FilesProvider(chequearr[i], "","Cheque"));
                            }
                        }
                    } else {
                        filesProviders.add(new FilesProvider(chequefile, filelink + chequefile,"Cheque"));
                    }
                }

                if(aadharfile.isEmpty())
                {

                }
                else {
                    Boolean a = aadharfile.contains(",");
                    if (a) {
                        String[] aadhararr = aadharfile.split(",");
                        for (int i = 0; i < aadhararr.length; i++) {
                            if (aadhararr[i].contains(".jpg") || aadhararr[i].contains(".jpeg")) {
                                filesProviders.add(new FilesProvider(aadhararr[i], filelink + aadhararr[i],"Aadhar"));

                            } else {
                                filesProviders.add(new FilesProvider(aadhararr[i], "","Aadhar"));
                            }
                        }

                    } else {
                        filesProviders.add(new FilesProvider(aadharfile, filelink + aadharfile,"Aadhar"));
                    }
                }

                if(tcfile.isEmpty())
                {
                }
                else {
                    Boolean t = tcfile.contains(",");
                    if (t) {
                        String[] tcarr = tcfile.split(",");
                        for (int i = 0; i < tcarr.length; i++) {
                            if (tcarr[i].contains(".jpg") || tcarr[i].contains(".jpeg")) {
                                filesProviders.add(new FilesProvider(tcarr[i], filelink + tcarr[i],"TC"));

                            } else {
                                filesProviders.add(new FilesProvider(tcarr[i], "","TC"));
                            }
                        }
                    } else {
                        filesProviders.add(new FilesProvider(tcfile, filelink + tcfile,"TC"));
                    }
                }

                filesAdapter = new FilesAdapter(filesProviders, activity,activity);
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                binding.rvFiles.scrollToPosition(0);
                binding.rvFiles.setLayoutManager(layoutManager1);
                binding.rvFiles.setAdapter(filesAdapter);

                binding.tvCustname.setText(name);
                binding.tvPanno.setText(pan);
                binding.tvGstNo.setText(gst);
                binding.tvTanNo.setText(tan);
                binding.tvPayment.setText(pay);
                binding.tvCreatedby.setText(emp);
                binding.tvApprover.setText(approver);
                binding.tvPcreditdays.setText(pcdays);
                binding.tvPcreditamount.setText(pcamount);
                binding.tvAcreditdays.setText(acdays);
                binding.tvAcreditamount.setText(acamount);
                binding.tvTeamname.setText(team);
                binding.tvSegmentname.setText(segment);
                binding.tvEmail.setText(email);
                binding.tvAddress.setText(address);
                binding.tvStatus.setText(status);
                binding.appBar.profieText.setText(date);

                binding.setIsLoading(false);
            }
        });
    }

    public void acceptRejectCall(String loginid, String historyid, String customerid, int approved, String apexbus, String apamount, String apdays, String remarks, String Token)
    {
        approvecafs(loginid,historyid,customerid,approved,apexbus,apamount,apdays,remarks,Token).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
                    String status = data.get(0).Status;
                    Toast.makeText(activity, status, Toast.LENGTH_SHORT).show();
                    activity.finish();
            }
        });
    }

    public void approveaction(int value)
    {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_popup_remarks);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        dialog.findViewById(R.id.btn_popup_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText remark = dialog.findViewById(R.id.edit_remarktext);
                remarks = remark.getText().toString();
                acceptRejectCall(loginid,historyid,customerid,value,apexbus,apamount,apdays,remarks,token);
                dialog.dismiss();
            }
        });
    }

}
