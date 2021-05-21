package com.fyp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.fyp.response.Pet;

public class PetDataSourceFactory extends DataSource.Factory<String, Pet> {
    private MutableLiveData<PageKeyedDataSource<String, Pet>> petLiveDataSource = new MutableLiveData<>();

    private String typeFilter;
    private String shelterFilter;

    public PetDataSourceFactory() {
        this.typeFilter = null;
        this.shelterFilter = null;
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

    public void setTypeFilter(String typeFilter) {
        this.typeFilter = typeFilter;
    }

    public void setShelterFilter(String shelterFilter) {
        this.shelterFilter = shelterFilter;
    }
}
