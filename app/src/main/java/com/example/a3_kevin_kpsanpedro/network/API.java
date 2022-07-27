package com.example.a3_kevin_kpsanpedro.network;

import com.example.a3_kevin_kpsanpedro.model.BotwCompendium;
import com.example.a3_kevin_kpsanpedro.model.BotwWeapon;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    //Base url of the api
    public final String BASE_URL = "https://botw-compendium.herokuapp.com/api/v2/category/";

    //endpoints we want to connect to
    //https://botw-compendium.herokuapp.com/api/v2/category/equipment


    @GET("equipment")
    public Call<BotwCompendium> getAllWeapon();

}
