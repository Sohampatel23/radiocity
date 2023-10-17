package com.app.radiocity.ViewModel;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.app.radiocity.other.SharedConst.AGENT;
import static com.app.radiocity.other.SharedConst.EMAIL;
import static com.app.radiocity.other.SharedConst.USERNAME;
import static com.app.radiocity.other.SharedConst.USER_DETAILS;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.radiocity.Repository.AuthenticateRepository;
import com.app.radiocity.View.ForgotPasswordActivity;
import com.app.radiocity.View.HomeActivity;
import com.app.radiocity.databinding.ActivityLoginScreenBinding;
import com.app.radiocity.model.LoginRecords;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.app.radiocity.R;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class LoginViewModel extends ViewModel implements View.OnClickListener {
    private Activity activity;
    private ActivityLoginScreenBinding binding;

    AuthenticateRepository authenticateRepository;

    public <T> LoginViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityLoginScreenBinding) binding;
        authenticateRepository = new AuthenticateRepository(activity);
        initview();
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding.btnLogin.setOnClickListener(this);
        binding.tvForgotTextButton.setOnClickListener(this);
    }

    /***
     * repository
     * **/
    public MutableLiveData<List<LoginRecords>> attemptUserLogin(String email, String password) {
        return authenticateRepository.getLoginDetails(email, password);
    }

    /**
     * onclick
     **/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                performLogin(view);

                break;
            case R.id.tv_forgotTextButton:
                performForgotButtonClick();
                break;
        }
    }

    private void loginNetworkIntegration(String loginid, String password) {
        attemptUserLogin(loginid, password).observe((LifecycleOwner) activity, data -> {
            if (data != null) {
                if(data.get(0).empID != null) {
                    Intent intent = new Intent(activity, HomeActivity.class);
                    SharedPrefUtils.saveBoolean(activity,SharedConst.ISLOGIN,true);
                    SharedPrefUtils.saveStringToUtils(activity,SharedConst.LOGINID,loginid);
                    SharedPrefUtils.saveStringToUtils(activity,SharedConst.TOKEN,data.get(0).Token);
                    SharedPrefUtils.saveStringToUtils(activity, USERNAME,data.get(0).FunctionName);
                    SharedPrefUtils.saveStringToUtils(activity,USER_DETAILS, new Gson().toJson(data.get(0)));

                    activity.startActivity(intent);
                    activity.finish();
                    binding.setIsLoading(false);
                }
            }
            else
            {
                Toast.makeText(activity,"Inavlid username/password",Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            }
        });
    }

    private void performLogin(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
        if (isValidate()) {
            binding.setIsLoading(true);
            loginNetworkIntegration(Objects.requireNonNull(binding.etEmailEdt.getText()).toString(), binding.etPasswordEdt.getText().toString());
        }
    }

    private boolean isValidate() {
        String email = Objects.requireNonNull(binding.etEmailEdt.getText()).toString();
        String password = Objects.requireNonNull(binding.etPasswordEdt.getText()).toString();
        if (email.isEmpty() ) {
           binding.etEmailEdt.setError("Email Id cannot be empty");
            return false;
        }
        if(password.isEmpty())
        {
            binding.etPasswordEdt.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void performForgotButtonClick() {
        Intent im = new Intent(activity, ForgotPasswordActivity.class);
        activity.startActivity(im);
    }
}
