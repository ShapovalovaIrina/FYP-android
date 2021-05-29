package com.fyp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.Pet;
import com.fyp.response.PetsWithMetadata;
import com.fyp.response.Shelter;
import com.fyp.response.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class PetDataSource extends PageKeyedDataSource<String, Pet> {
    private static final String TAG = PetDataSource.class.getSimpleName();
    public static final int LIMIT_SIZE = 5;

    private static final String DIRECTION_AFTER = "after";

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
        if (!SERVER_ENABLED) {
            callback.onResult(loadFakeData(typeFilter, shelterFilter), null, null);
            return;
        }
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
                        callback.onResult(new ArrayList<>(), null, null);
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Pet> callback) {
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
                        callback.onResult(new ArrayList<>(), params.key);
                    }
                });
    }

    private List<Pet> loadFakeData(String typeFilter, String shelterFilter) {
        List<Pet> petsList = new ArrayList<>();
        Shelter shelter = new Shelter(1, "Тестовый приют", "https://vk.com/habr", "https://yandex.ru/");
        Type cat = new Type(1, "Котик");
        Type dog = new Type(2, "Собака");
        List<String> catPhotos = new ArrayList<>();
        catPhotos.add("https://ololo.tv/wp-content/uploads/2021/01/248484_197.jpg");
        List<String> dogPhotos = new ArrayList<>();
        dogPhotos.add("https://i.pinimg.com/564x/ea/c1/0f/eac10f494d4f11ea9e27f8139a048571.jpg");

        petsList.add(new Pet("1", "First cat", "https://vk.com/photo-190703_456258182", catPhotos, cat, shelter));
        petsList.add(new Pet("2", "Second cat", "https://vk.com/photo-190703_456258182", catPhotos, cat, shelter));
        petsList.add(new Pet("3", "Third cat", "https://vk.com/photo-190703_456258182", catPhotos, cat, shelter));
        petsList.add(new Pet("4", "First dog", "https://vk.com/photo-190703_456258182", dogPhotos, dog, shelter));
        petsList.add(new Pet("5", "Second dog", "https://vk.com/photo-190703_456258182", dogPhotos, dog, shelter));
        petsList.add(new Pet("6", "Third dog", "https://vk.com/photo-190703_456258182", dogPhotos, dog, shelter));

        if (typeFilter != null) {
            List<String> typeFilterArray = Arrays.asList(typeFilter.split(","));
            petsList.removeIf(p -> !typeFilterArray.contains(Integer.toString(p.getType().getId())));
        }
        if (shelterFilter != null) {
            List<String> shelterFilterArray = Arrays.asList(shelterFilter.split(","));
            petsList.removeIf(p -> !shelterFilterArray.contains(Integer.toString(p.getShelter().getId())));
        }

        return petsList;
    }
}
