package com.app.radiocity.ViewModel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.radiocity.R;
import com.app.radiocity.Repository.AgencyAutoRepository;
import com.app.radiocity.Repository.AutocompleteRepository;
import com.app.radiocity.Repository.ClientAutoRepository;
import com.app.radiocity.Repository.PendingCafRepository;
import com.app.radiocity.databinding.ActivityAddCafBinding;
import com.app.radiocity.databinding.ActivityForgotPasswordBinding;
import com.app.radiocity.model.AutoAgencyModel;
import com.app.radiocity.model.AutoClientModel;
import com.app.radiocity.model.AutoTeamModel;
import com.app.radiocity.model.PendingCAFModel;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.SharedPrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddCafViewModel extends ViewModel implements View.OnClickListener{

    private Activity activity;
    private ActivityAddCafBinding binding;
    String loginid,token;
    String name;

    String suggestedText;
    AutocompleteRepository autocompleteRepository;
    ClientAutoRepository clientAutoRepository;
    AgencyAutoRepository agencyAutoRepository;
    Calendar calendar;
    private ArrayList<String> clients = new ArrayList<>();
    private ArrayList<String> agencys = new ArrayList<>();

    public <T> AddCafViewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityAddCafBinding) binding;
        autocompleteRepository = new AutocompleteRepository(activity);
        clientAutoRepository = new ClientAutoRepository(activity);
        agencyAutoRepository = new AgencyAutoRepository(activity);
        calendar = Calendar.getInstance();
        initview();
    }

    public MutableLiveData<List<AutoTeamModel>> getteams(String loginid, String Token, String prefix) {
        return autocompleteRepository.autocompleteteam(loginid,Token,prefix);
    }

    public MutableLiveData<List<AutoClientModel>> getclients(String loginid, String Token, String prefix) {
        return clientAutoRepository.autocompleteclient(loginid,Token,prefix);
    }

    public MutableLiveData<List<AutoAgencyModel>> getagency(String loginid, String Token, String prefix) {
        return agencyAutoRepository.autocompleteagency(loginid,Token,prefix);
    }


    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        loginid = SharedPrefUtils.getStringUtils(activity, SharedConst.LOGINID);
        token = SharedPrefUtils.getStringUtils(activity, SharedConst.TOKEN);

        binding.appBar.appBarTitle.setText("Incentivizer");
        binding.etClientName.setOnClickListener(this);
        binding.etAgencyName.setOnClickListener(this);
        binding.etTeamName.setOnClickListener(this);
        autoClient("");
        autoAgency("");

        binding.appBar.backIconImageButton.setOnClickListener(this);

        binding.etTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                if(charSequence.length()>1)
//                {
//                   String v =  autoCompleteTeam(loginid,token,charSequence.toString());
//                   binding.etTeamName.setText(v);
//
//
//                }

                if(charSequence.length()!=0)
                {
                    suggestedText= autoCompleteTeam(charSequence.toString());

                    for(int k =charSequence.length()+1;k<=suggestedText.length();k++)
                    {
                        binding.etTeamName.setText(suggestedText.charAt(k));
                        binding.etTeamName.setTextColor(Color.GRAY);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

//               suggestedText= autoCompleteTeam(loginid,token,editable.toString());
//                binding.ouputtext.setText(suggestedText);
//                binding.ouputtext.setVisibility(View.VISIBLE);
//                // to avoid infinite loops
//                if(suggestedText!=null && !"".equals(suggestedText) && !suggestedText.equals(binding.etTeamName.getText().toString()))
//                {
//                    // add a onclick control to update the input
//                    binding.ouputtext.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            binding.etTeamName.setText(suggestedText);
//                        }
//                    });
//                }
            }
        });

        binding.etClientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0) {
                    autoClient(charSequence.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                autoClient(loginid,token,editable.toString());
            }
        });

        binding.etAgencyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0) {
                    autoAgency(charSequence.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                autoAgency(loginid,token,editable.toString());
            }
        });

        int selectedid1 = binding.rgSegment.getCheckedRadioButtonId();
        RadioButton rb1 = activity.findViewById(selectedid1);
        String v1 = rb1.getText().toString();

        int selectedid2 = binding.rgInventory.getCheckedRadioButtonId();
        RadioButton rb2 = activity.findViewById(selectedid2);
        String v2 = rb2.getText().toString();

        int selectedid3 = binding.rgInventory.getCheckedRadioButtonId();
        RadioButton rb3 = activity.findViewById(selectedid2);
        String v3 = rb3.getText().toString();

        binding.etStartDate.setFocusable(false);
        binding.etStartDate.setClickable(true);

        binding.etStartDate.setOnClickListener(this);

        binding.etEndDate.setFocusable(false);
        binding.etEndDate.setClickable(true);
        binding.etEndDate.setOnClickListener(this);
    }

    public String autoCompleteTeam(String prefix)
    {

        getteams(loginid,token,prefix).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
               name  = data.get(1).Team_Name;
            }
        });
        return name;
    }

    public void autoClient(String prefix)
    {
        getclients(loginid,token,prefix).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
                clients.clear();
                for(int i=1;i<=data.size()-1;i++)
                {
                    clients.add(data.get(i).Customer_Name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, clients);
                    binding.etClientName.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void autoAgency(String prefix)
    {
        getagency(loginid,token,prefix).observe((LifecycleOwner) activity, data ->{
            if(data!=null)
            {
                agencys.clear();
                for(int i=1;i<=data.size()-1;i++)
                {
                    agencys.add(data.get(i).Customer_Name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, agencys);
                    binding.etAgencyName.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void openDateDialog() {
        SimpleDateFormat df;
        df = new SimpleDateFormat("dd-MM-yyyy");

        DatePickerDialog.OnDateSetListener mydate = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.etStartDate.setText(df.format(calendar.getTime()));
        };

        new DatePickerDialog(activity, mydate, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openEndDateDialog() {
        SimpleDateFormat df;
        df = new SimpleDateFormat("dd-MM-yyyy");

        DatePickerDialog.OnDateSetListener mydate = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.etEndDate.setText(df.format(calendar.getTime()));
        };

        new DatePickerDialog(activity, mydate, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.backIconImageButton:
                activity.finish();
                break;

            case R.id.et_start_date:

                openDateDialog();
                break;

            case R.id.et_end_date:
                openEndDateDialog();
                break;

            case R.id.et_client_name:
                binding.etClientName.showDropDown();
                break;

            case R.id.et_agency_name:
                binding.etAgencyName.showDropDown();
                break;
        }
    }
}
