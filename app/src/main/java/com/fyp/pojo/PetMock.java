package com.fyp.pojo;

import com.fyp.response.Shelter;
import com.fyp.response.Type;

public class PetMock {
    private String id;
    private String name;
    private Type type;
    private int resourceId;
    private Shelter shelter;

    public PetMock(String id, String name, int resourceId, Type type, Shelter shelter) {
        this.id = id;
        this.name = name;
        this.resourceId = resourceId;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
                ", type=" + type +
                ", resourceId=" + resourceId +
                ", shelter=" + shelter +
                '}';
    }
}
