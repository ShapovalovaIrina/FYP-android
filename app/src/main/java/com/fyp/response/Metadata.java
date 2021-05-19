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

    public void setBefore(String before) {
        this.before = before;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isTotal_count_cap_exceeded() {
        return total_count_cap_exceeded;
    }

    public void setTotal_count_cap_exceeded(boolean total_count_cap_exceeded) {
        this.total_count_cap_exceeded = total_count_cap_exceeded;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "after='" + after + '\'' +
                ", before='" + before + '\'' +
                ", limit=" + limit +
                ", total_count=" + total_count +
                ", total_count_cap_exceeded=" + total_count_cap_exceeded +
                '}';
    }
}
