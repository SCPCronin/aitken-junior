package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    @JsonProperty("album")
    private Album album;

    @JsonProperty("artists")
    private List<Artist> artists;

    @JsonProperty("available_markets")
    private List<String> availableMarkets;

    @JsonProperty("disc_number")
    private int discNumber;

    @JsonProperty("duration_ms")
    private int duration;

    @JsonProperty("explicit")
    private Boolean explicit;

    @JsonProperty("external_ids")
    private ExternalID externalIDs;

    @JsonProperty("external_urls")
    private ExternalUrl externalUrl;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("is_playable")
    private Boolean isPlayable;

    @JsonProperty("linked_from")
    private Map<String, String>  linkedFrom;

    @JsonProperty("restriciton")
    private Restriction restriction;

    @Getter
    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("track_number")
    private int trackNumber;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("is_local")
    private Boolean isLocal;

}
