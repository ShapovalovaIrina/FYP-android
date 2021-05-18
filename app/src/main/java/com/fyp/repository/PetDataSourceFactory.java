package com.fyp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.fyp.response.Pet;

public class PetDataSourceFactory extends DataSource.Factory<String, Pet> {
    private final String TAG = PetDataSourceFactory.class.getSimpleName();
    private MutableLiveData<PageKeyedDataSource<String, Pet>> petLiveDataSource = new MutableLiveData<>();

    private String typeFilter;
    private String shelterFilter;

    public PetDataSourceFactory(String typeFilter, String shelterFilter) {
        this.typeFilter = typeFilter;
        this.shelterFilter = shelterFilter;
    }

    @NonNull
    @Override
    public DataSource<String, Pet> create() {
        PetDataSource petDataSource = new PetDataSource(typeFilter, shelterFilter);
        petLiveDataSource.postValue(petDataSource);
        return petDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<String, Pet>> getPetLiveDataSource() {
        return petLiveDataSource;
    }
}
