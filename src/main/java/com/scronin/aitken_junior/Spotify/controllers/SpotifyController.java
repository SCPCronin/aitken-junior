package com.scronin.aitken_junior.Spotify.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scronin.aitken_junior.Spotify.exceptions.SpotifyServiceException;
import com.scronin.aitken_junior.Spotify.model.Album;
import com.scronin.aitken_junior.Spotify.model.Artist;
import com.scronin.aitken_junior.Spotify.model.SavedTrack;
import com.scronin.aitken_junior.Spotify.model.Track;
import com.scronin.aitken_junior.Spotify.services.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/test")
    public String test() {
        System.out.println("Testing..");
        return "Abc123";
    }

    @GetMapping("/track/{trackID}")
    public ResponseEntity<?> getTrack(@PathVariable String trackID) {
        try {
            Track track = this.spotifyService.getTrack(trackID);
            if (track != null) {
                return ResponseEntity.ok(track);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SpotifyServiceException e) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/savedTracks")
    public ResponseEntity<List<SavedTrack>> getMyProfile() {
        List<SavedTrack> tracks = this.spotifyService.getUsersSavedTracks();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/album/{albumID}")
    public ResponseEntity<?> getAlbum(@PathVariable String albumID) {
        try {
            Album album = this.spotifyService.getAlbum(albumID);
            if (album != null) {
                return ResponseEntity.ok(album);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SpotifyServiceException e) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/artist/{artistID}")
    public ResponseEntity<?> getArtist(@PathVariable String artistID) {
        try {
            Artist artist = this.spotifyService.getArtist(artistID);
            if (artist != null) {
                return ResponseEntity.ok(artist);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SpotifyServiceException e) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
