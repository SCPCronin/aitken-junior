package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    @JsonProperty("url")
    private String url;

    @JsonProperty("height")
    private int height;

    @JsonProperty("width")
    private int width;
}
