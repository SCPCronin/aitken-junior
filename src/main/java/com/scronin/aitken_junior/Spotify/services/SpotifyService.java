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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        String token = this.authService.getAccessToken();
        List<SavedTrack> savedTracks = new ArrayList<>();
        String nextLink = "/me/tracks?limit=50";

        try {
            while (nextLink != null && !Objects.equals(nextLink, "null")) {
                String responseString = fetchTracks(token, nextLink);
                JsonNode rootNode = objectMapper.readTree(responseString);
                JsonNode itemsNode = rootNode.path("items");
                List<SavedTrack> tracks = objectMapper.readValue(itemsNode.toString(), new TypeReference<List<SavedTrack>>() {});
                savedTracks.addAll(tracks);
                nextLink = rootNode.path("next").isNull() ? null : rootNode.path("next").asText();
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error Occurred: " + e.getMessage());
            return new ArrayList<>();
        }
        return savedTracks;
    }

    private String fetchTracks(String token, String uri) {
        return webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * {@inheritDoc}
     */
    public int getSavedTrackCount() {
        return getUsersSavedTracks().size();
    }

    /**
     * {@inheritDoc}
     */
    public Track getTrack(String trackID) {
        String token = this.authService.getAccessToken();

        try {
            String responseString = webClient.get()
                    .uri("/tracks/" + trackID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SpotifyServiceException("Track Not Found", 1001));
                        }
                        return response.bodyToMono(String.class);
                    })
                    .block();

            if (responseString == null || responseString.isEmpty()) {
                throw new SpotifyServiceException("Track Not Found or empty response", 1001);
            }

            Track track = objectMapper.readValue(responseString, Track.class);
            if (track == null) {
                throw new SpotifyServiceException("Track Not Found or could not parse response", 1001);
            }
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
        String token = this.authService.getAccessToken();

        try {
            String responseString = webClient.get()
                    .uri("/albums/" + albumID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SpotifyServiceException("Album Not Found", 1002));
                        }
                        return response.bodyToMono(String.class);
                    })
                    .block();

            if (responseString == null || responseString.isEmpty()) {
                throw new SpotifyServiceException("Album Not Found or empty response", 1002);
            }

            Album album = objectMapper.readValue(responseString, Album.class);
            if (album == null) {
                throw new SpotifyServiceException("Album Not Found or could not parse response", 1002);
            }
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
        String token = this.authService.getAccessToken();

        try {
            String responseString = webClient.get()
                    .uri("/artists/" + artistID)
                    .header("Authorization", "Bearer " + token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                            return Mono.error(new SpotifyServiceException("Artist Not Found", 1003));
                        }
                        return response.bodyToMono(String.class);
                    })
                    .block();

            if (responseString == null || responseString.isEmpty()) {
                throw new SpotifyServiceException("Artist Not Found or empty response", 1003);
            }

            Artist artist = objectMapper.readValue(responseString, Artist.class);
            if (artist == null) {
                throw new SpotifyServiceException("Artist Not Found or could not parse response", 1003);
            }
            return artist;
        } catch (SpotifyServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to fetch artist: " + artistID, 1003);
        }
    }
}

