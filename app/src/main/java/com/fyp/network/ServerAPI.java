package com.fyp.network;

import com.fyp.response.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerAPI {
    @GET("/pets")
    Call<List<Pet>> getAllPets();

    @GET("/users/favourite")
    Call<List<Pet>> getAllFavourite(
            @Header("Authorization") String token
    );

    @POST("/users/favourite/{pet_id}")
    Call<List<Pet>> addFavourite(
            @Header("Authorization") String token,
            @Path("pet_id") String petId
    );

    @DELETE("/users/favourite/{pet_id}")
    Call<List<Pet>> removeFavourite(
            @Header("Authorization") String token,
            @Path("pet_id") String petId
    );
}
