package com.fyp.pojo;

import com.fyp.response.Shelter;

public class PetMock {
    private String id;
    private String name;
    private int resourceId;
    private Shelter shelter;

    public PetMock(String id, String name, int resourceId, Shelter shelter) {
        this.id = id;
        this.name = name;
        this.resourceId = resourceId;
        this.shelter = shelter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public String toString() {
        return "PetMock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resourceId=" + resourceId +
                '}';
    }
}
