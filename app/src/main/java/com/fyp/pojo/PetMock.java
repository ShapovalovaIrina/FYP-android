package com.fyp.pojo;

public class PetMock {
    private String id;
    private String name;
    private int resourceId;

    public PetMock(String id, String name, int resourceId) {
        this.id = id;
        this.name = name;
        this.resourceId = resourceId;
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

    @Override
    public String toString() {
        return "PetMock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resourceId=" + resourceId +
                '}';
    }
}
