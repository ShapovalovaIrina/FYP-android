package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Type {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("type")
    @Expose
    private String type;

    public Type() {
    }

    public Type(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
