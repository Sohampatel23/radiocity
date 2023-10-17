package com.app.radiocity.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.radiocity.Repository.ForgotPasswordRepository;
import com.app.radiocity.View.LoginScreenActivity;
import com.app.radiocity.R;
import com.app.radiocity.databinding.ActivityForgotPasswordBinding;
import com.app.radiocity.model.ForgotPasswordModel;
import com.app.radiocity.model.LoginRecords;

import java.util.List;

public class ForgotViewModel extends ViewModel implements View.OnClickListener {
    private Activity activity;
    private ActivityForgotPasswordBinding binding;
    ForgotPasswordRepository forgotPasswordRepository;

    public <T> ForgotViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityForgotPasswordBinding)binding;
        forgotPasswordRepository = new ForgotPasswordRepository(activity);
        initview();
    }

    public MutableLiveData<List<ForgotPasswordModel>> sendemail(String loginid) {
        return forgotPasswordRepository.sendmail(loginid);
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding.btnForgotSendButton.setOnClickListener(this);
        binding.loginTextButton.setOnClickListener(this);
        binding.appBar.backIconImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forgotSendButton:
                performForgotPassword();
                break;
            case R.id.loginTextButton:
                performMoveToLogin();
                break;
            case R.id.backIconImageButton:
                performBackPressed();
                break;
        }
    }

    private void performForgotPassword() {
        binding.setIsLoading(true);
        String fpwd = binding.etFwgEmailId.getText().toString();
        if(fpwd.isEmpty())
        {
            binding.etFwgEmailId.setError("Email Id cannot be empty");
            binding.setIsLoading(false);
        }
        else
        {
          String loginid = binding.etFwgEmailId.getText().toString();
          sendemailid(loginid);
        }

    }

    private void performBackPressed() {
        activity.onBackPressed();
    }

    private void performMoveToLogin() {
        Intent m = new Intent(activity, LoginScreenActivity.class);
        m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(m);
    }

    private void sendemailid(String loginid){
        sendemail(loginid).observe((LifecycleOwner) activity, data -> {
            if(data != null)
            {
                if(data.get(0).Status!="Invalid UserName !!! ")
                {
                    Toast.makeText(activity,data.get(0).Status,Toast.LENGTH_SHORT).show();
                    binding.setIsLoading(false);
                }
                else
                {
                    Toast.makeText(activity,data.get(0).Status,Toast.LENGTH_SHORT).show();
                    binding.setIsLoading(false);
                }
            }
        });
    }
}
