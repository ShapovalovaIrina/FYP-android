package com.fyp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fyp.network.RetrofitClient;
import com.fyp.network.ServerAPI;
import com.fyp.response.PetBody;
import com.fyp.response.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.constant.Constants.SERVER_ENABLED;

public class PetRepository {
    private final String TAG = PetRepository.class.getSimpleName();
    private ServerAPI serverAPI;

    public PetRepository() {
        serverAPI = RetrofitClient.getRetrofitInstance().create(ServerAPI.class);
    }

    public void addPet(
            String JWTToken,
            PetBody pet,
            @NonNull final MutableLiveData<Integer> codeResponse) {
        if (!SERVER_ENABLED) {
            return;
        }
        serverAPI.addPet(JWTToken, pet).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.d(TAG, "addPet onResponse response");
                switch (response.code()) {
                    case 400:
                        Log.d(TAG, "addPet onResponse 400 Bad status");
                        codeResponse.setValue(400);
                        break;
                    case 401:
                        Log.d(TAG, "addPet onResponse 401 Unauthenticated");
                        codeResponse.setValue(401);
                        break;
                    case 403:
                        Log.d(TAG, "addPet onResponse 403 Access forbidden");
                        codeResponse.setValue(403);
                        break;
                    case 201:
                        codeResponse.setValue(201);
                        break;
                    default:
                        Log.d(TAG, "addPet onResponse other code " + response.code());
                        codeResponse.setValue(500);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "addPet onFailure response: " + t.getMessage());
                codeResponse.setValue(500);
            }
        });
    }
}
