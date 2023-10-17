package com.app.radiocity.network;


import com.app.radiocity.model.LoginCallModel;
import com.app.radiocity.model.LoginModel;
import com.app.radiocity.model.LoginOutput;
import com.app.radiocity.model.LoginRecords;
import com.app.radiocity.model.PayoutBaseModel;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
//    @Multipart
    @POST("CreateCAF")
    @Headers({"Content-Type: application/x-www-form-urlencoded", "Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> addcaf(@Field("loginId") String loginId,
                        @Field("Token") String token,
                        @Field("customerid") String customerid,
                        @Field("Customer_Name") String Customer_Name,
                        @Field("IsDirectCustomer") String IsDirectCustomer,
                        @Field("Email")String Email,
                        @Field("TanNo_NotAvailable") String TanNo_NotAvailable,
                        @Field("TANNo") String TANNo,
                        @Field("TypeOfCustomer") String TypeOfCustomer,
                        @Field("TypeOfCustomer_Name")String TypeOfCustomer_Name,
                        @Field("PAN") String PAN,
                        @Field("GSTNo_NotAvailable") String GSTNo_NotAvailable,
                        @Field("GSTNo") String GSTNo,
                        @Field("ContactPersonName") String ContactPersonName,
                        @Field("ContactPersonNo") String ContactPersonNo,
                        @Field("ContactPersonMobile") String ContactPersonMobile,
                        @Field("PhoneNumber") String PhoneNumber,
                        @Field("ROContactPerson") String ROContactPerson,
                        @Field("ROContactNo")  String ROContactNo,
                        @Field("PaymentTerms") String PaymentTerms,
                        @Field("CreditAmount") String CreditAmount,
                        @Field("CreditDays") String CreditDays,
                        @Field("ExpectedBusinessAmount") String ExpectedBusinessAmount,
                        @Field("CurrentBusinessAmount") String CurrentBusinessAmount,
                        @Field("TeamId") String TeamId,
                        @Field("SegmentId") String SegmentId,
                        @Field("ProductId") String ProductId,
                        @Field("BrandName") String BrandName,
                        @Field("SubmitLaterDocs")  String SubmitLaterDocs,
                        @Field("SubmitLaterDocsDate") String SubmitLaterDocsDate,
                        @Field("AddressLine1") String AddressLine1,
                        @Field("Street1") String Street1,
                        @Field("Street2") String Street2,
                        @Field("District") String District,
                        @Field("StateId") String StateId,
                        @Field("CityId") String CityId,
                        @Field("PinCode") String PinCode,
                        @Field("Country") String Country,
                        @Field("TANFileName") String TANFileName,
                        @Field("TANbase64") String TANbase64,
                        @Field("GSTNoFileName") String GSTNoFileName,
                        @Field("GSTNobase64")  String GSTNobase64,
                        @Field("PANFileName") String PANFileName,
                        @Field("PANbase64")  String PANbase64,
                        @Field("ChequeFileName")  String ChequeFileName,
                        @Field("Chequebase64") String Chequebase64,
                        @Field("TNCFileName") String TNCFileName,
                        @Field("TNCbase64") String TNCbase64,
                        @Field("AadharFileName") String AadharFileName,
                        @Field("Aadharbase64") String Aadharbase64,
                        @Field("CIN") String CIN,
                        @Field("AgencyType") String AgencyType);

    @FormUrlEncoded
    @POST("LoginMAP")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
    //@Headers({"Accept: application/json"})
    Call<String> getdetails(@Field("loginId") String loginId, @Field("password") String password);

    @FormUrlEncoded
    @POST("ForgotPassword")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> sendmail(@Field("loginId") String loginId);

    @FormUrlEncoded
    @POST("PendingCAFList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> pendingcafs(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetDropdownList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> getdropdown(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GSTN_Validation")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> gstvalidation(@Field("loginId") String loginId, @Field("Token") String Token, @Field("PAN") String pan, @Field("GSTN") String gst);

    @FormUrlEncoded
    @POST("PAN_Validation")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> panvalidation(@Field("loginId") String loginId, @Field("Token") String Token, @Field("PAN") String pan);

    @FormUrlEncoded
    @POST("GetMenuAccess")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> getmenu(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetFileDownloadHandlerURL")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> filedownload(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetTypeOfCustomer")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> gettype(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetPaymentTerms")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> getpayment(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetAndroidVersion")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> appversion(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("MobileLogout")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> logout(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("ViewCAF")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewcafs(@Field("loginId") String loginId, @Field("CustomerId") String CustomerId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetTeamForAutoComplete")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> autocompleteteam(@Field("loginId") String loginId, @Field("Token") String Token, @Field("prefixText") String prefixText);

    @FormUrlEncoded
    @POST("GetClientForAutoComplete")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> autocompleteclient(@Field("loginId") String loginId, @Field("Token") String Token, @Field("prefixText") String prefixText);

    @FormUrlEncoded
    @POST("GetAgencyForAutoComplete")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> autocompleteagency(@Field("loginId") String loginId, @Field("Token") String Token, @Field("prefixText") String prefixText);

    @FormUrlEncoded
    @POST("LoadCustomer")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> loadcustomer(@Field("loginId") String loginId, @Field("CustomerId") String CustomerId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetTeamList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewTeamList(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetSegmentList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewSegmentList(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetProductList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewProductList(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GetStateList")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewStateList(@Field("loginId") String loginId, @Field("Token") String Token);

    @FormUrlEncoded
    @POST("GeCityListByStateId")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> viewCityList(@Field("loginId") String loginId, @Field("Token") String Token, @Field("StateId") String StateId);

    @FormUrlEncoded
    @POST("ApproveCAF")
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
        //@Headers({"Accept: application/json"})
    Call<String> approveCAF(@Field("loginId") String loginId, @Field("HistoryId") String historyid, @Field("CustomerId") String CustomerId, @Field("Approved") int Approved, @Field("ApExpBus") String ApExpBus, @Field("ApAmount") String ApAmount, @Field("ApDays") String ApDays, @Field("Remarks") String Remarks, @Field("Token") String Token);


}
