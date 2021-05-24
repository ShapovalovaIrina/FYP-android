package com.fyp.network;

import com.fyp.response.Pet;
import com.fyp.response.PetBody;
import com.fyp.response.PetsWithMetadata;
import com.fyp.response.Shelter;
import com.fyp.response.Status;
import com.fyp.response.Type;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerAPI {
    /* Pets */
    @GET("/pets/chunks")
    Call<PetsWithMetadata> getPetsChunks(
            @Query("type_id") String typeArray,
            @Query("shelter_id") String shelterArray,
            @Query("limit") int limit,
            @Query("cursor") String cursor,
            @Query("direction") String direction
    );

    @POST("/pets")
    Call<Status> addPet(
            @Header("Authorization") String token,
            @Body PetBody pet
    );

    /* Favourites */
    @GET("/users/favourite")
    Call<List<Pet>> getAllFavourite(
            @Header("Authorization") String token
    );

    @POST("/users/favourite/{pet_id}")
    Call<Status> addFavourite(
            @Header("Authorization") String token,
            @Path("pet_id") String petId
    );

    @DELETE("/users/favourite/{pet_id}")
    Call<Status> removeFavourite(
            @Header("Authorization") String token,
            @Path("pet_id") String petId
    );

    /* Shelters */
    @GET("/shelters")
    Call<List<Shelter>> getAllShelters();

    @POST("/shelters")
    Call<Status> createShelter(
            @Header("Authorization") String token,
            @Body Shelter shelter
    );


    /* Types */
    @GET("/types")
    Call<List<Type>> getAllTypes();
}
