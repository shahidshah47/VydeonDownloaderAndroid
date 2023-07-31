package com.intellicoder.vydeondownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class ModelFeedDetails implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("latest_reel_media")
    private long latest_reel_media;
    @SerializedName("expiring_atexpiring_at")
    private long expiring_at;
    @SerializedName("seen")
    private long seen;
    @SerializedName("reel_type")
    private String reel_type;

    @SerializedName("items")
    private ArrayList<ModelInstaItem> items;
    @SerializedName("media_count")
    private int media_count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLatest_reel_media() {
        return latest_reel_media;
    }

    public void setLatest_reel_media(long latest_reel_media) {
        this.latest_reel_media = latest_reel_media;
    }

    public long getExpiring_at() {
        return expiring_at;
    }

    public void setExpiring_at(long expiring_at) {
        this.expiring_at = expiring_at;
    }

    public long getSeen() {
        return seen;
    }

    public void setSeen(long seen) {
        this.seen = seen;
    }

    public String getReel_type() {
        return reel_type;
    }

    public void setReel_type(String reel_type) {
        this.reel_type = reel_type;
    }

    public ArrayList<ModelInstaItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ModelInstaItem> items) {
        this.items = items;
    }

    public int getMedia_count() {
        return media_count;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }
}
