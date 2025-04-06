package com.scronin.aitken_junior.Spotify.interfaces;

import com.scronin.aitken_junior.Spotify.model.Album;
import com.scronin.aitken_junior.Spotify.model.Artist;
import com.scronin.aitken_junior.Spotify.model.Track;

import java.util.List;

public interface ISpotifyService {

    /**
     * Get all the saved tracks for the user who generated the token.
     *
     * @return A list of Track Objects, which contains all tracks the user has saved
     */
    List<Track> getSavedTracks();

    /**
     * Get the count of the saved tracks for the user
     *
     * @return an int with the number of tracks the user has saved
     */
    int getSavedTrackCount();

    /**
     * Get a Track object, given a Track ID
     *
     * @param trackID - The ID of the requested track
     * @return The Track object, of the track with requested ID
     */
    Track getTrack(String trackID);

    /**
     * Get an Album Object, given an Album ID
     *
     * @param albumID - The ID of the requested album
     * @return The Album object, of the album with requested ID
     */
    Album getAlbum(String albumID);

    /**
     * Get an Artist Object, given an Artist ID
     *
     * @param artistID - The ID of the requested album
     * @return The Artist object, of the artist with requested ID
     */
    Artist getArtist(String artistID);

}
