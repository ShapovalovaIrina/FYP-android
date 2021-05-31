package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

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

    @SerializedName("source_link")
    @Expose
    private String sourceLink;

    @SerializedName("photos")
    @Expose
    private List<String> photos;

    @SerializedName("shelter")
    @Expose
    private Shelter shelter;

    @SerializedName("type")
    @Expose
    private Type type;

    public Pet(String id, String name, String sourceLink, List<String> photos, Type type, Shelter shelter) {
        this.id = id;
        this.name = name;
        this.sourceLink = sourceLink;
        this.photos = photos;
        this.shelter = shelter;
        this.type = type;
    }

    public String getBirth() {
        return birth;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public String getFirstPhoto() {
        return photos.get(0);
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(birth, pet.birth) &&
                Objects.equals(breed, pet.breed) &&
                Objects.equals(description, pet.description) &&
                Objects.equals(gender, pet.gender) &&
                Objects.equals(height, pet.height) &&
                id.equals(pet.id) &&
                name.equals(pet.name) &&
                sourceLink.equals(pet.sourceLink) &&
                Objects.equals(photos, pet.photos) &&
                Objects.equals(shelter, pet.shelter) &&
                Objects.equals(type, pet.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(birth, breed, description, gender, height, id, name, sourceLink, photos, shelter, type);
    }
}
