package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artist {

    @JsonProperty("external_urls")
    private ExternalUrl externalUrl;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

}
