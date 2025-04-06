package com.scronin.aitken_junior.Spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private Album album;
    private List<Artist> artists;
    private List<String> availableMarkets;
    private int discNumber;
    private int duration;
    private Boolean explicit;
    private ExternalID externalId;
    private ExternalUrl externalUrl;
    private String href;
    private String id;
    private Boolean isPlayable;
    private Map<String, String>  linkedFrom;
    private Restriction restriction;
    private String name;
    private int popularity;
    private String previewUrl;
    private int trackNumber;
    private String type;
    private String uri;
    private Boolean isLocal;

}
