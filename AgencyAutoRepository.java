package com.app.radiocity.Repository;

import static com.app.radiocity.other.SharedConst.ISLOGIN;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.radiocity.View.LoginScreenActivity;
import com.app.radiocity.model.AutoAgencyModel;
import com.app.radiocity.model.AutoClientModel;
import com.app.radiocity.network.ApiCall;
import com.app.radiocity.network.ApiService;
import com.app.radiocity.network.NetworkConnectionInterceptor;
import com.app.radiocity.other.DialogUtil;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgencyAutoRepository {

    private Activity activity;
    private ApiService apiService;
    DialogUtil dialogUtils;
    ArrayList<AutoAgencyModel> Models = new ArrayList<>();

    public AgencyAutoRepository(Activity activity) {
        this.activity = activity;
        dialogUtils = new DialogUtil(activity);
    }

    public MutableLiveData<List<AutoAgencyModel>> autocompleteagency(String loginid, String token, String prefix) {
        apiService = ApiCall.getClient(activity).create(ApiService.class);
        MutableLiveData<List<AutoAgencyModel>> list = new MutableLiveData<>();
        Call<String> call = apiService.autocompleteagency(loginid, token, prefix);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("Records");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("output");
                        JSONObject j1 = jsonArray1.getJSONObject(0);
                        if(Objects.equals(j1.getString("Status"),"TokenValid")) {
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.e("json size",jsonArray.length()+"");
                                    Models.add(new Gson().fromJson(String.valueOf(jsonArray.get(i)),AutoAgencyModel.class));
                                    if(i==jsonArray.length()-1) {
                                        list.setValue(Models);
                                    }
                                }
                            }
                    }
                        else
                        {
                            Toast.makeText(activity, j1.getString("Status"), Toast.LENGTH_SHORT).show();
                            Intent im = new Intent(activity, LoginScreenActivity.class);
                            SharedPrefUtils.saveBoolean(activity, ISLOGIN, false);
                            SharedPrefUtils.clearSharedUtils(activity);
                            im.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            im.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            im.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(im);
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
                catch (Exception ex)
                {
                    ex.printStackTrace();
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
