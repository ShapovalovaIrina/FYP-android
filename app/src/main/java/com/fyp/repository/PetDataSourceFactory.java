package com.fyp.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.fyp.response.Pet;

public class PetDataSourceFactory extends DataSource.Factory<String, Pet> {
    private MutableLiveData<PageKeyedDataSource<String, Pet>> petLiveDataSource = new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource<String, Pet> create() {
        PetDataSource petDataSource = new PetDataSource();
        petLiveDataSource.postValue(petDataSource);
        return petDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<String, Pet>> getPetLiveDataSource() {
        return petLiveDataSource;
    }
}
