package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pet {
    @SerializedName("birth")
    @Expose
    private String birth;

    @SerializedName("breed")
    @Expose
    private String breed;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("photos")
    @Expose
    private List<String> photos;

    @SerializedName("shelter")
    @Expose
    private Shelter shelter;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getFirstPhoto() {
        return photos.get(0);
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "birth='" + birth + '\'' +
                ", breed='" + breed + '\'' +
                ", description='" + description + '\'' +
                ", gender='" + gender + '\'' +
                ", height='" + height + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photos=" + photos +
                ", shelter=" + shelter +
                '}';
    }
}
