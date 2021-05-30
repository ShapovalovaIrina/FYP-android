package com.fyp.response;

import java.util.List;

public class PetBody {
    private String name;
    private String source;
    private int type_id;
    private String breed;
    private String birth;
    private String description;
    private String gender;
    private String height;
    private List<String> photos;
    private int shelter_id;

    public PetBody(String name, String source, int type_id, String breed, String birth, String description, String gender, String height, List<String> photos, int shelter_id) {
        this.name = name;
        this.source = source;
        this.type_id = type_id;
        this.breed = breed;
        this.birth = birth;
        this.description = description;
        this.gender = gender;
        this.height = height;
        this.photos = photos;
        this.shelter_id = shelter_id;
    }
}
