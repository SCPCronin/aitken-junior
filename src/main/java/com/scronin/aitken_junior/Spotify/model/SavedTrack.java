package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SavedTrack {

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("track")
    private Track track;

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    @JsonProperty("added_at")
    private String addedAt;
}
