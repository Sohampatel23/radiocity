package com.app.radiocity.Repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.radiocity.model.AppVersionModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.network.ApiCall;
import com.app.radiocity.network.ApiService;
import com.app.radiocity.network.NetworkConnectionInterceptor;
import com.app.radiocity.other.DialogUtil;
import com.app.radiocity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppVersionRepository {
    private Activity activity;
    private ApiService apiService;
    DialogUtil dialogUtils;
    ArrayList<AppVersionModel> appVersionModels = new ArrayList<>();

    public AppVersionRepository(Activity activity) {
        this.activity = activity;
        dialogUtils = new DialogUtil(activity);
    }

    public MutableLiveData<List<AppVersionModel>> appversion(String loginid, String token) {
        apiService = ApiCall.getClient(activity).create(ApiService.class);
        MutableLiveData<List<AppVersionModel>> list = new MutableLiveData<>();
        Call<String> call = apiService.appversion(loginid, token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try
                {
                    if(response.isSuccessful())
                    {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("Records");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("output");
                        JSONObject j1 = jsonArray1.getJSONObject(0);
                        if(Objects.equals(j1.getString("Status"),"Success")) {
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.e("json size",jsonArray.length()+"");
                                    appVersionModels.add(new Gson().fromJson(String.valueOf(jsonArray.get(i)),AppVersionModel.class));
                                    if(i==jsonArray.length()-1) {
                                        list.setValue(appVersionModels);
                                    }
                                }
                            }
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
                catch (Exception e)
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
