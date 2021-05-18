package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PetsWithMetadata {
    @SerializedName("entries")
    @Expose
    private List<Pet> entries;

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public List<Pet> getEntries() {
        return entries;
    }

    public void setEntries(List<Pet> entries) {
        this.entries = entries;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
