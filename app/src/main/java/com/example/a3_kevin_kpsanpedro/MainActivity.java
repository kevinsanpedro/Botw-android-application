package com.example.a3_kevin_kpsanpedro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.a3_kevin_kpsanpedro.adapters.ItemAdapter;
import com.example.a3_kevin_kpsanpedro.databinding.ActivityMainBinding;
import com.example.a3_kevin_kpsanpedro.databinding.CustomRowLayoutBinding;
import com.example.a3_kevin_kpsanpedro.model.BotwCompendium;
import com.example.a3_kevin_kpsanpedro.model.BotwWeapon;
import com.example.a3_kevin_kpsanpedro.network.API;
import com.example.a3_kevin_kpsanpedro.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnRowItemClickListener{
    // tag
    private final String TAG="Response";

    //binding
    private ActivityMainBinding binding;

    //api
    private API api;

    // the list of characters returned by the api
    private List<BotwWeapon> WeaponList = new ArrayList<>();
    private List<BotwWeapon> WeaponResult=new ArrayList<>();
    private ArrayList<String> location=new ArrayList<>();
    private String textFromSpinner ="";

    //recylerview
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //spinner
        location.add("Choose a location");
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, R.layout.custom_dropdown_menu_item, location);
        binding.spinner.setAdapter(locationAdapter);
        binding.spinner.getSelectedItem();

        //recyclerview
        adapter = new ItemAdapter(this, WeaponResult, this::OnItemClickListener);

        //update recyclerview
        binding.rvWeaponItems.setAdapter(this.adapter);
        binding.rvWeaponItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWeaponItems.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        //config the API client
        //Connect to endpoint and retrieve the data
        //- create a call network request
        // - do async network request "enqueue"
        this.api = RetrofitClient.getInstance().getAPI();
        Call<BotwCompendium> request = api.getAllWeapon();
        request.enqueue(new Callback<BotwCompendium>() {
            @Override
            public void onResponse(Call<BotwCompendium> call, Response<BotwCompendium> response) {
                if(response.isSuccessful()==false ){
                    Log.d(TAG, "Error from API with response code: " + response.code());
                    return;
                }

                BotwCompendium obj = response.body();
                WeaponList = obj.getData();

                //get the common_location and add it into the local variable location, with duplicate
                for(int i=0;i<WeaponList.size();i++ ){
                    if(WeaponList.get(i).getCommon_locations()!=null){
                        for(int j=0; j< WeaponList.get(i).getCommon_locations().size();j++){
                            location.add(WeaponList.get(i).getCommon_locations().get(j));
                        }
                    }
                }

                //User hashset location to assign only unique value
                //- clear old location then replace it with only unique location
                //- add all unique location to the location
                HashSet<String> hashSetLocation = new HashSet<String>(location);
                location.clear();
                location.add("Choose a location");

                for(String uniqueLocation : hashSetLocation){
                    if( uniqueLocation != "Choose a location")
                        location.add(uniqueLocation);
                }

                //spinner listener
                binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //if user select "choose a location" replace it with empty string
                        textFromSpinner = location.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //search button handler
                //if user choose a location set to empty string
                //if text box and spinner is not selected show a snack bar and clear recycler view
                //if both SearchTextFromUI and textFromSpinner is NOT empty show a snack bar and ask user to choose only one
                //if SearchTextFromUI is empty replace it with the selected value from the spinner
                //clear the recycler view to make sure we delete the previous search if any
                //use loop to search by location
                //compare weapon common location to SearchTextFromUI
                // -  if common location and SearchTextFromUI is same
                // - add weaponList to the weaponResult
                //notify the Recycler adapter that the data has been updated
                //reset the value for spinner and text box
                binding.searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String SearchTextFromUI = binding.etSearchBox.getText().toString();
                        if(textFromSpinner=="Choose a location"){
                            textFromSpinner = "";
                        }
                        if(SearchTextFromUI.isEmpty() && textFromSpinner.isEmpty() ){
                            Snackbar.make(binding.getRoot(), "Clear screen!", Snackbar.LENGTH_SHORT).show();
                            WeaponResult.clear();
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        if (!SearchTextFromUI.isEmpty() && !textFromSpinner.isEmpty()) {
                            Snackbar.make(binding.getRoot(), "Used only one textbox or spinner", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if(SearchTextFromUI.isEmpty() &&!textFromSpinner.isEmpty()) {
                            SearchTextFromUI = textFromSpinner;
                        }

                        binding.tvResult.setText(SearchTextFromUI);
                        WeaponResult.clear();

                        for(int i=0;i<WeaponList.size();i++ ){
                            if(WeaponList.get(i).getCommon_locations()!=null){
                                if(WeaponList.get(i).getCommon_locations().contains(SearchTextFromUI))
                                    WeaponResult.add(WeaponList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();


                        binding.spinner.setSelection(0);
                        textFromSpinner = "";
                        binding.etSearchBox.setText("");

                    }
                });
            }
            @Override
            public void onFailure(Call<BotwCompendium> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    //incase we want to add a listener when user click the item
    @Override
    public void OnItemClickListener(BotwWeapon weapon) {
        Snackbar.make(binding.getRoot(),  weapon.getName(), Snackbar.LENGTH_SHORT).show();
    }
}