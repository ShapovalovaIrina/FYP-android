package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {
    @SerializedName("after")
    @Expose
    private String after;

    @SerializedName("before")
    @Expose
    private String before;

    @SerializedName("limit")
    @Expose
    private int limit;

    @SerializedName("total_count")
    @Expose
    private int total_count;

    @SerializedName("total_count_cap_exceeded")
    @Expose
    private boolean total_count_cap_exceeded;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }
}
