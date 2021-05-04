package com.fyp.network;

import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ServerAPI {
    @GET("/pets")
    Call<List<Pet>> getAllPets();

    @GET("/users/favourite")
    Call<List<Pet>> getAllFavourite(@Header("Authorization") String token);
}
