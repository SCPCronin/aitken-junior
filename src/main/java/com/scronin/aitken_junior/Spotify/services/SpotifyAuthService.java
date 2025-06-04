package com.scronin.aitken_junior.Spotify.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SpotifyAuthService {

    private String clientId = System.getenv("SPOTIFY_CLIENT_ID");

    private String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

    private final String refreshToken = System.getenv("SPOTIFY_REFRESH_TOKEN");

    private final WebClient webClient = WebClient.create("https://accounts.spotify.com");

    public String getAccessToken() {
        return webClient.post()
                .uri("/api/token")
                .header("Authorization", this.getBasicAuthHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=refresh_token&refresh_token=" + refreshToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block(); // <-- block here to get String immediately
    }

    private String getBasicAuthHeader() {
        String credentials = clientId + ":" + clientSecret;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}

