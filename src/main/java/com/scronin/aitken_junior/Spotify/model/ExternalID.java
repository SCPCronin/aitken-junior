package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalID {

    @JsonProperty("isrc")
    private String isrc;

    @JsonProperty("ean")
    private String ean;

    @JsonProperty("upc")
    private String upc;

}
