package com.app.radiocity.Repository;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.radiocity.Utils;
import com.app.radiocity.model.AddCafModel;
import com.app.radiocity.model.GetPaymentModel;
import com.app.radiocity.network.ApiCall;
import com.app.radiocity.network.ApiService;
import com.app.radiocity.network.NetworkConnectionInterceptor;
import com.app.radiocity.other.DialogUtil;
import com.app.radiocity.preferences.DialogUtils;
import com.github.barteksc.pdfviewer.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCafRepository {
    private Activity activity;
    private ApiService apiService;
    DialogUtils dialogUtils;
    DialogUtil dialogUtil;

    ArrayList<AddCafModel> addCafModels = new ArrayList<>();

    public AddCafRepository(Activity activity) {
        this.activity = activity;
        dialogUtils = new DialogUtils(activity);
        dialogUtil = new DialogUtil(activity);

    }

    public MutableLiveData<String> addcaf(String loginid, String token,String customerid,String Customer_Name,String IsDirectCustomer,
                                                     String Email,String TanNo_NotAvailable, String TANNo,String TypeOfCustomer,
                                                     String TypeOfCustomer_Name,String PAN,String GSTNo_NotAvailable, String GSTNo,
                                                     String ContactPersonName,String ContactPersonNo,String ContactPersonMobile,String PhoneNumber,
                                                     String ROContactPerson, String ROContactNo,String PaymentTerms,String CreditAmount,
                                                     String CreditDays,String ExpectedBusinessAmount, String CurrentBusinessAmount,String TeamId,
                                                     String SegmentId,String ProductId,String BrandName, String SubmitLaterDocs,
                                                     String SubmitLaterDocsDate,String AddressLine1,String Street1,String Street2,
                                                     String District,String StateId, String CityId,String PinCode,String Country,
                                                     String TANFileName,String TANbase64,String GSTNoFileName,
                                                     String GSTNobase64,String PANFileName,
                                                     String PANbase64, String ChequeFileName,String Chequebase64,
                                                     String TNCFileName, String TNCbase64,
                                                     String AadharFileName, String Aadharbase64,
                                                     String CIN,String AgencyType){

        apiService = ApiCall.getClient(activity).create(ApiService.class);
        final MutableLiveData<String> list = new MutableLiveData<>();
        Call<String> call = apiService.addcaf(loginid,  token, customerid, Customer_Name, IsDirectCustomer,
                 Email, TanNo_NotAvailable,  TANNo, TypeOfCustomer,
                 TypeOfCustomer_Name, PAN, GSTNo_NotAvailable,  GSTNo,
                 ContactPersonName, ContactPersonNo, ContactPersonMobile, PhoneNumber,
                 ROContactPerson,  ROContactNo, PaymentTerms, CreditAmount,
                 CreditDays, ExpectedBusinessAmount,  CurrentBusinessAmount, TeamId,
                 SegmentId, ProductId, BrandName,  SubmitLaterDocs,
                 SubmitLaterDocsDate, AddressLine1, Street1, Street2,
                 District, StateId,  CityId, PinCode, Country,
                 TANFileName, TANbase64,   GSTNoFileName,
                GSTNobase64,  PANFileName,
                PANbase64,  ChequeFileName, Chequebase64,
                TNCFileName,  TNCbase64,
                 AadharFileName,  Aadharbase64,
                 CIN, AgencyType);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try{
                    if(response.isSuccessful())
                    {
                        Log.e("output body",response.body());
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("Records");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("output");
                        JSONObject jb = jsonArray1.getJSONObject(0);
                        String st = jb.getString("Status");
                       list.setValue(st);
                    }
                    else {

                        try {
                            String str = new Gson().toJson(response.errorBody().string());
//                            JSONObject jsonObject = new JSONObject(str);
                            Log.e("output body",str);

                            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
                            Toast.makeText(activity, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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
//                dialogUtils.hideprogress(activity);
                if(t instanceof NetworkConnectionInterceptor.NoConnectivityException) {
                    list.setValue(null);
                    t.getLocalizedMessage();
                    t.printStackTrace();
                    dialogUtil.showNoInternetDialog();
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
