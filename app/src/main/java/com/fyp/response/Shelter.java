package com.fyp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shelter {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("site_link")
    @Expose
    private String site_link;

    @SerializedName("vk_link")
    @Expose
    private String vk_link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite_link() {
        return site_link;
    }

    public void setSite_link(String site_link) {
        this.site_link = site_link;
    }

    public String getVk_link() {
        return vk_link;
    }

    public void setVk_link(String vk_link) {
        this.vk_link = vk_link;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "title='" + title + '\'' +
                ", site_link='" + site_link + '\'' +
                ", vk_link='" + vk_link + '\'' +
                '}';
    }
}
