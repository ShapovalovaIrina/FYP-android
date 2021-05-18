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
    //the size of a page that we want
    public static final int LIMIT_SIZE = 5;

    private static String CURSOR_AFTER = null;
    private static String CURSOR_BEFORE = null;

    private static final String DIRECTION_AFTER = "after";
    private static final String DIRECTION_BEFORE = "before";

    private ServerAPI serverAPI;

    public PetDataSource() {
        super();
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, Pet> callback) {
        serverAPI.getPetsChunks(null, null, LIMIT_SIZE, CURSOR_AFTER, DIRECTION_AFTER)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "loadInitial. Response metadata : " + response.body().getMetadata().toString());
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
        serverAPI.getPetsChunks(null, null, LIMIT_SIZE, params.key, DIRECTION_BEFORE)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "loadBefore. Response body size: " + response.body().getEntries().size());
                            for (Pet pet : response.body().getEntries()) {
                                Log.d(TAG, pet.getName());
                            }
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
        Log.d(TAG, "loadAfter. Key value : " + params.key);
        serverAPI.getPetsChunks(null, null, LIMIT_SIZE, params.key, DIRECTION_AFTER)
                .enqueue(new Callback<PetsWithMetadata>() {
                    @Override
                    public void onResponse(Call<PetsWithMetadata> call, Response<PetsWithMetadata> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "loadAfter. Response metadata : " + response.body().getMetadata().toString());
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
