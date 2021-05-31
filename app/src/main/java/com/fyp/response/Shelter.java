package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shelter {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("site_link")
    @Expose
    private String site_link;

    @SerializedName("vk_link")
    @Expose
    private String vk_link;

    public Shelter() {
    }

    public Shelter(int id, String title, String site_link, String vk_link) {
        this.id = id;
        this.title = title;
        this.site_link = site_link;
        this.vk_link = vk_link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSite_link() {
        return site_link;
    }

    public String getVk_link() {
        return vk_link;
    }

}
