package com.app.radiocity.ViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.app.radiocity.R;
import com.app.radiocity.Repository.AddCafRepository;
import com.app.radiocity.Repository.CreateCAFRepository;
import com.app.radiocity.Repository.DropDownRepository;
import com.app.radiocity.Repository.GetPaymentRepository;
import com.app.radiocity.Repository.GetTypeCustomerRepository;
import com.app.radiocity.Repository.PendingCafRepository;
import com.app.radiocity.Repository.ValidationRepository;
import com.app.radiocity.databinding.ActivityCreateCafBinding;
import com.app.radiocity.model.AddCafModel;
import com.app.radiocity.model.AgencyModel;
import com.app.radiocity.model.CityModel;
import com.app.radiocity.model.GSTModel;
import com.app.radiocity.model.GetPaymentModel;
import com.app.radiocity.model.PANModel;
import com.app.radiocity.model.ProductModel;
import com.app.radiocity.model.SegmentModel;
import com.app.radiocity.model.StateModel;
import com.app.radiocity.model.TeamModel;
import com.app.radiocity.model.TypeofCustomerModel;
import com.app.radiocity.model.ViewCAFModel;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateCAFViewModel extends ViewModel implements View.OnClickListener{
    private Activity activity;
    private ActivityCreateCafBinding binding;
    String loginid,token,customerid;
    String state,cityID;
    private ArrayList<String> teamIds = new ArrayList<>(),segmentIds = new ArrayList<>(),productIds = new ArrayList<>(),customertypes = new ArrayList<>(),
    stateIds = new ArrayList<>(),cityIds = new ArrayList<>(),payments = new ArrayList<>(),agencytype = new ArrayList<>();


    private DropDownRepository dropDownRepository;
    private ValidationRepository validationRepository;
    private int PICK_IMAGE_REQUEST = 1;


    public <T> CreateCAFViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityCreateCafBinding) binding;

        dropDownRepository = new DropDownRepository(activity);
        validationRepository = new ValidationRepository(activity);
        initView();
    }

    private void initView() {

    binding.appBar.appBarTitle.setText("Create CAF");

        loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);


        binding.appBar.backIconImageButton.setOnClickListener(this);
        binding.etTeamId.setOnClickListener(this);
        binding.etSegmentId.setOnClickListener(this);
        binding.etProductId.setOnClickListener(this);
        binding.etStateId.setOnClickListener(this);

       binding.etStateId.setOnItemClickListener((adapterView, view, i, l) -> {
           getCityList();
       });
        binding.etCityId.setOnClickListener(this);

        binding.etTypeOfCustomer.setOnClickListener(this);
        binding.etPaymentTerms.setOnClickListener(this);
        binding.etAgencyType.setOnClickListener(this);


        getTeamList();
        getSegmentList();
        getProductList();
        getStateList();
        getCustomerType();
        getPaymentTerms();
        getAgencyTypes();

        getStateid();
        getCityid();
        getTeamid();
        getSegmentid();
        getProductid();

        binding.btnCreateCAFSubmit.setOnClickListener(this);

        binding.etGSTNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String gst = binding.etGSTNO.getText().toString();
                if(gst.isEmpty())
                {

                }
                else
                {
                    GSTvalidate();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etPAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0) {
                    PANvalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public MutableLiveData<List<GSTModel>> validategst(String loginid, String token, String pan, String gst) {
        return validationRepository.gstvalidation(loginid,token,pan, gst);
    }

    public MutableLiveData<List<PANModel>> validatepan(String loginid, String token, String pan) {
        return validationRepository.panvalidation(loginid,token,pan);
    }

    public MutableLiveData<List<CityModel>> viewCityLists(String loginid, String token, String stateId) {
        return dropDownRepository.cityListLiveData(loginid,token,stateId);
    }

    public MutableLiveData<List<TeamModel>> getteams(String loginid, String token) {
        return dropDownRepository.getteams(loginid,token);
    }

    public MutableLiveData<List<SegmentModel>> getsegments(String loginid, String token) {
        return dropDownRepository.getsegments(loginid,token);
    }

    public MutableLiveData<List<StateModel>> getstates(String loginid, String token) {
        return dropDownRepository.getstate(loginid,token);
    }

    public MutableLiveData<List<ProductModel>> getproducts(String loginid, String token) {
        return dropDownRepository.getproducts(loginid,token);
    }

    public MutableLiveData<List<GetPaymentModel>> getpaymenterms(String loginid, String token) {
        return dropDownRepository.getpayments(loginid,token);
    }

    public MutableLiveData<List<AgencyModel>> getagencies(String loginid, String token) {
        return dropDownRepository.getagency(loginid,token);
    }

    public MutableLiveData<List<TypeofCustomerModel>> getcustomerypes(String loginid, String token) {
        return dropDownRepository.getcustomertype(loginid,token);
    }

    public void GSTvalidate()
    {
        validategst(loginid,token,binding.etPAN.getText().toString(),binding.etGSTNO.getText().toString()).observe((LifecycleOwner) activity,data -> {
            if(data!=null)
            {
                if(!data.get(0).GSTNNo.isEmpty())
                {
                    binding.etGSTNO.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    binding.etGSTnoNotAvailable.setText("false");
                    binding.etGSTnoNotAvailable.setEnabled(false);
                }
            }
            else
            {
                binding.etGSTNO.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                binding.etGSTnoNotAvailable.setText("true");
                binding.etGSTnoNotAvailable.setEnabled(false);
            }
        });

    }

    public void PANvalidate()
    {
        Log.e("pann",binding.etPAN.getText().toString());
        validatepan(loginid,token,binding.etPAN.getText().toString()).observe((LifecycleOwner) activity,data -> {
            if(data!=null)
            {

                if(!data.get(0).GSTNNo.isEmpty())
                {
                    if(data.get(0).GSTNNo.equals("NOT APPLICABLE"))
                    {
                        binding.etGSTNO.setText("");
                    }
                    else {
                        binding.etGSTNO.setText(data.get(0).GSTNNo);
                    }
                        binding.etPAN.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    Log.e("data name",data.get(0).Customer_Name);
                        binding.etCustomerName.setText(data.get(0).Customer_Name);
                    Log.e("customer name",binding.etCustomerName.getText().toString());
                    binding.etCityId.setText(data.get(0).City);

                        customerid = data.get(0).Customer_ID;
                }
            }
            else
            {
                binding.etPAN.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                binding.etCustomerName.setText("");
                binding.etCityId.setText("");
                binding.etGSTNO.setText("");
                binding.etGSTnoNotAvailable.setText("");
            }
        });

    }

    private void getTeamList() {
        getteams(loginid,token).observe((LifecycleOwner)activity, data -> {
            if(data!=null)
            {
                teamIds.clear();
                for(int i=0;i<=data.size();i++)
                {
                    teamIds.add(data.get(i).Team_Name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, teamIds);
                    binding.etTeamId.setAdapter(adapter);
                }

            }
        });
    }

    private void getTeamid()
    {
        binding.etTeamId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String staten = parent.getItemAtPosition(position).toString();
                getteams(loginid,token).observe((LifecycleOwner)activity,data -> {
                    if(data!=null)
                    {
                        for(int i=0;i<=data.size();i++)
                        {
                            if(data.get(i).Team_Name.equals(staten))
                            {
                                binding.tvteamid.setText(data.get(i).Team_ID);
                            }
                        }

                    }
                });
            }
        });


    }

    private void getSegmentList() {
        getsegments(loginid,token).observe((LifecycleOwner)activity,data -> {
            if(data!=null)
            {
                segmentIds.clear();
                for(int i=0;i<=data.size();i++)
                {
                    segmentIds.add(data.get(i).SEGMENT_Name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, segmentIds);
                    binding.etSegmentId.setAdapter(adapter);
                }

            }
        });
    }

    private void getSegmentid()
    {
        binding.etSegmentId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String staten = parent.getItemAtPosition(position).toString();
                getsegments(loginid,token).observe((LifecycleOwner)activity,data -> {
                    if(data!=null)
                    {
                        for(int i=0;i<=data.size();i++)
                        {
                            if(data.get(i).SEGMENT_Name.equals(staten))
                            {
                                binding.tvsegmentid.setText(data.get(i).SEGMENT_ID);
                            }
                        }
                    }
                });
            }
        });
    }

    private void getProductList() {
        getproducts(loginid,token).observe((LifecycleOwner)activity,data -> {
            if(data!=null)
            {
                productIds.clear();
                for(int i=0;i<=data.size();i++)
                {
                    productIds.add(data.get(i).Product_Name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, productIds);
                    binding.etProductId.setAdapter(adapter);
                }
            }
        });
    }

    private void getProductid()
    {
        binding.etProductId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String staten = parent.getItemAtPosition(position).toString();
                getproducts(loginid,token).observe((LifecycleOwner)activity,data -> {
                    if(data!=null)
                    {
                        for(int i=0;i<=data.size();i++)
                        {
                            if(data.get(i).Product_Name.equals(staten))
                            {
                                binding.tvproductid.setText(data.get(i).Product_ID);
                            }
                        }

                    }
                });
            }
        });
    }

    private void getStateList() {
        getstates(loginid,token).observe((LifecycleOwner)activity,data -> {
            if(data!=null)
            {
                stateIds.clear();
                for(int i=0;i<=data.size();i++)
                {
                    stateIds.add(data.get(i).StateName);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, stateIds);
                    binding.etStateId.setAdapter(adapter);
                }

            }
        });
    }

    private void getStateid()
    {
        binding.etStateId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String staten = parent.getItemAtPosition(position).toString();
                getstates(loginid,token).observe((LifecycleOwner)activity,data -> {
                    if(data!=null)
                    {
                        for(int i=0;i<=data.size();i++)
                        {
                            if(data.get(i).StateName.equals(staten))
                            {
                                binding.tvstateid.setText(data.get(i).StateID);
                                getCityList();
                            }
                        }

                    }
                });
            }
        });
    }

    private void getCityList() {
        cityIds.clear();
            viewCityLists(loginid, token, binding.tvstateid.getText().toString()).observe((LifecycleOwner) activity, data -> {
                if (data != null) {
                    for (int i = 0; i <= data.size(); i++) {
                        cityIds.add(data.get(i).City_Name);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                                android.R.layout.simple_list_item_1, cityIds);
                        binding.etCityId.setAdapter(adapter);
                    }

                }
            });
    }

    private void getCityid()
    {
        binding.etCityId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String staten = parent.getItemAtPosition(position).toString();
                viewCityLists(loginid,token, binding.tvstateid.getText().toString()).observe((LifecycleOwner)activity,data -> {
                    if(data!=null)
                    {
                        for(int i=0;i<=data.size();i++)
                        {
                            if(data.get(i).City_Name.equals(staten))
                            {
                                binding.tvcityid.setText(data.get(i).City_ID);
                            }
                        }

                    }
                });
            }
        });
    }

    private void getCustomerType() {
        getcustomerypes(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                customertypes.clear();;
                for(int i=0;i<=data.size();i++)
                {
                    customertypes.add(data.get(i).Key);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, customertypes);
                    binding.etTypeOfCustomer.setAdapter(adapter);
                }

            }
        });
    }

    private void getPaymentTerms() {
        getpaymenterms(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                payments.clear();
                for(int i=0;i<=data.size();i++)
                {
                    payments.add(data.get(i).Key);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, payments);
                    binding.etPaymentTerms.setAdapter(adapter);
                }

            }
        });
    }

    private void getAgencyTypes() {
        getagencies(loginid,token).observe((LifecycleOwner) activity,data ->{
            if(data!=null)
            {
                agencytype.clear();
                for(int i=0;i<=data.size();i++)
                {
                    agencytype.add(data.get(i).Key);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, agencytype);
                    binding.etAgencyType.setAdapter(adapter);
                }

            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backIconImageButton:
                activity.finish();
                break;
                
            case R.id.et_team_id:
                binding.etTeamId.showDropDown();
                break;

            case R.id.et_segment_id:
                binding.etSegmentId.showDropDown();
                break;

            case R.id.et_product_id:
                binding.etProductId.showDropDown();
                break;

            case R.id.et_state_id:
                binding.etStateId.showDropDown();
                break;

            case R.id.et_city_id:
                binding.etCityId.showDropDown();
                break;

            case R.id.et_payment_terms:
                binding.etPaymentTerms.showDropDown();
                break;

            case R.id.et_type_of_customer:
                binding.etTypeOfCustomer.showDropDown();
                break;

            case R.id.et_agency_type:
                binding.etAgencyType.showDropDown();
                break;

//
                

        }
    }





}
