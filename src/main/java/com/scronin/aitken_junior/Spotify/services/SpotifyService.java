package com.scronin.aitken_junior.Spotify.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scronin.aitken_junior.Spotify.exceptions.SpotifyServiceException;
import com.scronin.aitken_junior.Spotify.interfaces.ISpotifyService;
import com.scronin.aitken_junior.Spotify.model.Album;
import com.scronin.aitken_junior.Spotify.model.Artist;
import com.scronin.aitken_junior.Spotify.model.SavedTrack;
import com.scronin.aitken_junior.Spotify.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SpotifyService implements ISpotifyService {

    private final WebClient webClient = WebClient.create("https://api.spotify.com/v1");

    @Autowired
    private SpotifyAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     */
    public List<SavedTrack> getUsersSavedTracks() {
        log.debug("Getting users saved tracks");
        String token = this.authService.getAccessToken();
        List<SavedTrack> savedTracks = new ArrayList<>();
        log.debug("Access token fetched");
        String nextLink = "/me/tracks?limit=50";

        try {
            log.debug("Fetching tracks from the Spotify API using Next Link: {}", nextLink);
            while (nextLink != null && !Objects.equals(nextLink, "null")) {
                String responseString = this.fetchTracks(token, nextLink);
                JsonNode rootNode = objectMapper.readTree(responseString);
                JsonNode itemsNode = rootNode.path("items");
                List<SavedTrack> tracks = objectMapper.readValue(itemsNode.toString(), new TypeReference<List<SavedTrack>>() {});
                savedTracks.addAll(tracks);
                nextLink = rootNode.path("next").isNull() ? null : rootNode.path("next").asText();
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON response while fetching saved tracks: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("An error occurred while fetching saved tracks: {}", e.getMessage());
            throw new SpotifyServiceException("Failed to fetch saved tracks", 1000);
        }
        return savedTracks;
    }

    private String fetchTracks(String token, String uri) {
        try {
            return webClient.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(body -> log.info("Successfully fetched tracks from URI: {}", uri))
                    .doOnError(error -> log.error("Failed to fetch tracks from URI: {} - {}",
                            uri, error.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Exception occurred while fetching tracks from URI: {}", uri, e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getSavedTrackCount() {
        log.debug("Getting saved track count");
        return getUsersSavedTracks().size();
    }

    /**
     * {@inheritDoc}
     */
    public Track getTrack(String trackID) {
        log.debug("Getting a specific track");
        String token = this.authService.getAccessToken();
        try {
            String responseString = webClient.get()
                    .uri("/tracks/" + trackID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            log.error("Response status is 404. Track not found: {}", trackID);
                            return Mono.error(new SpotifyServiceException("Track Not Found", 1001));
                        }
                        return response.bodyToMono(String.class);
                    })
                    .block();

            log.debug("Request made to fetch track.");
            if (responseString == null || responseString.isEmpty()) {
                log.error("Response string is null or empty for track ID: {}, response: {}", trackID, responseString);
                throw new SpotifyServiceException("Track Not Found or empty response", 1001);
            }

            Track track = objectMapper.readValue(responseString, Track.class);
            if (track == null) {
                log.error("Parsed track is null for track ID: {}, response: {}", trackID, responseString);
                throw new SpotifyServiceException("Track Not Found or could not parse response", 1001);
            }
            log.debug("Successfully fetched track: {}", track.getName());
            return track;
        } catch (SpotifyServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to fetch track: " + trackID, 1001);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Album getAlbum(String albumID) {
        log.debug("Getting a specific album");
        String token = this.authService.getAccessToken();
        try {
            String responseString = webClient.get()
                    .uri("/albums/" + albumID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            log.error("Response status is 404. Album not found: {}", albumID);
                            return Mono.error(new SpotifyServiceException("Album Not Found", 1002));
                        }
                        log.debug("Response status is OK. Fetching album data for ID: {}", albumID);
                        return response.bodyToMono(String.class);
                    })
                    .block();

            if (responseString == null || responseString.isEmpty()) {
                log.error("Response string is null or empty for album ID: {}, response: {}", albumID, responseString);
                throw new SpotifyServiceException("Album Not Found or empty response", 1002);
            }

            Album album = objectMapper.readValue(responseString, Album.class);
            if (album == null) {
                log.error("Parsed album is null for album ID: {}, response: {}", albumID, responseString);
                throw new SpotifyServiceException("Album Not Found or could not parse response", 1002);
            }
            log.debug("Successfully fetched album: {}", album.getName());
            return album;
        } catch (SpotifyServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to fetch album: " + albumID, 1002);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Artist getArtist(String artistID) {
        log.debug("Getting a specific artist");
        String token = this.authService.getAccessToken();

        try {
            String responseString = webClient.get()
                    .uri("/artists/" + artistID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            log.error("Response status is 404. Artist not found: {}", artistID);
                            return Mono.error(new SpotifyServiceException("Artist Not Found", 1003));
                        }
                        return response.bodyToMono(String.class);
                    })
                    .block();

            if (responseString == null || responseString.isEmpty()) {
                log.error("Response string is null or empty for artist ID: {}, response: {}", artistID, responseString);
                throw new SpotifyServiceException("Artist Not Found or empty response", 1003);
            }

            Artist artist = objectMapper.readValue(responseString, Artist.class);
            if (artist == null) {
                log.error("Parsed artist is null for artist ID: {}, response: {}", artistID, responseString);
                throw new SpotifyServiceException("Artist Not Found or could not parse response", 1003);
            }
            return artist;
        } catch (SpotifyServiceException e) {
            log.error("SpotifyServiceException occurred: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to fetch artist: " + artistID, 1003);
        }
    }
}

