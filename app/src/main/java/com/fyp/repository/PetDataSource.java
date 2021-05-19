package com.fyp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Pet;
import com.fyp.response.PetsWithMetadata;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetDataSource extends PageKeyedDataSource<String, Pet> {
    private static final String TAG = PetDataSource.class.getSimpleName();
    public static final int LIMIT_SIZE = 5;

    private static final String DIRECTION_AFTER = "after";
    private static final String DIRECTION_BEFORE = "before";

    private String typeFilter;
    private String shelterFilter;

    private ServerAPI serverAPI;

    public PetDataSource(String typeFilter, String shelterFilter) {
        super();
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
        this.typeFilter = typeFilter;
        this.shelterFilter = shelterFilter;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, Pet> callback) {
        Log.d(TAG, "PetDataSource loadInitial. Type filter: " + typeFilter + ". Shelter filter: " + shelterFilter);
        serverAPI.getPetsChunks(typeFilter, shelterFilter, LIMIT_SIZE, null, DIRECTION_AFTER)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "loadInitial. Response body size: " + response.body().getEntries().size());
                            for (Pet pet : response.body().getEntries()) {
                                Log.d(TAG, pet.getName());
                            }
                            callback.onResult(
                                    response.body().getEntries(),
                                    response.body().getMetadata().getBefore(),
                                    response.body().getMetadata().getAfter());
                        }
                    }

                    @Override
                    public void onFailure(Call<PetsWithMetadata> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Pet> callback) {
        Log.d(TAG, "PetDataSource loadBefore. Type filter: " + typeFilter + ". Shelter filter: " + shelterFilter);
        serverAPI.getPetsChunks(typeFilter, shelterFilter, LIMIT_SIZE, params.key, DIRECTION_BEFORE)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            callback.onResult(
                                    response.body().getEntries(),
                                    response.body().getMetadata().getBefore());
                        }
                    }

                    @Override
                    public void onFailure(Call<PetsWithMetadata> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Pet> callback) {
        Log.d(TAG, "PetDataSource loadAfter. Type filter: " + typeFilter + ". Shelter filter: " + shelterFilter);
        serverAPI.getPetsChunks(typeFilter, shelterFilter, LIMIT_SIZE, params.key, DIRECTION_AFTER)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "loadAfter. Response body size: " + response.body().getEntries().size());
                            for (Pet pet : response.body().getEntries()) {
                                Log.d(TAG, pet.getName());
                            }
                            callback.onResult(
                                    response.body().getEntries(),
                                    response.body().getMetadata().getAfter());
                        }
                    }

                    @Override
                    public void onFailure(Call<PetsWithMetadata> call, Throwable t) {

                    }
                });
    }
}
