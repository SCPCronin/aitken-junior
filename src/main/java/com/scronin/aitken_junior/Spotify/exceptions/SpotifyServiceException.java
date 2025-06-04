package com.scronin.aitken_junior.Spotify.exceptions;

public class SpotifyServiceException extends RuntimeException {

    public SpotifyServiceException(String message, int errorCode) {
        super(message + " (Error Code: " + errorCode + ")");
    }
}