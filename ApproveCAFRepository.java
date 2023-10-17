package com.app.radiocity.Repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.radiocity.model.ApproveCAFModel;
import com.app.radiocity.model.LoginRecords;
import com.app.radiocity.network.ApiCall;
import com.app.radiocity.network.ApiService;
import com.app.radiocity.network.NetworkConnectionInterceptor;
import com.app.radiocity.other.DialogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveCAFRepository {
    private Activity activity;
    private ApiService apiService;
    DialogUtil dialogUtils;
    ArrayList<ApproveCAFModel> approveCAFModels = new ArrayList<>();

    public ApproveCAFRepository(Activity activity) {
        this.activity = activity;
        dialogUtils = new DialogUtil(activity);
    }

    public MutableLiveData<List<ApproveCAFModel>> approveCAF(String loginid, String historyid, String customerid, int approved, String apexbus, String apamount, String apdays, String remarks, String Token) {
        apiService = ApiCall.getClient(activity).create(ApiService.class);
        MutableLiveData<List<ApproveCAFModel>> list = new MutableLiveData<>();
        Call<String> call = apiService.approveCAF(loginid,historyid,customerid,approved,apexbus,apamount,apdays,remarks,Token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try
                {
                    if(response.isSuccessful())
                    {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray1 = jsonObject.getJSONArray("output");
                        JSONObject j1 = jsonArray1.getJSONObject(0);
                            if (jsonArray1 != null) {
                                    Log.e("added",String.valueOf(jsonArray1.get(0)));
                                    approveCAFModels.add(new Gson().fromJson(String.valueOf(jsonArray1.get(0)),ApproveCAFModel.class));
                                    list.setValue(approveCAFModels);
                        }
                        else
                        {
                            list.setValue(null);
                        }
                    }
                    else {
                        try {
                            String str = new Gson().toJson(response.errorBody().string());
                            JSONObject jsonObject = new JSONObject(str);
                            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
                            list.setValue(null);

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            list.setValue(null);
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(t instanceof NetworkConnectionInterceptor.NoConnectivityException) {
                    list.setValue(null);
                    t.getLocalizedMessage();
                    t.printStackTrace();
                    dialogUtils.showNoInternetDialog();
                }
                else {
                    list.postValue(null);
                    t.printStackTrace();
                    Toast.makeText(activity, "Invalid Login Credentials !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return list;
    }
}
