package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    /**
     * Fetches all artists saved in the database and returns them.
     *
     * @return The list of found artists.
     */
    List<Artist> getAllArtists();

    /**
     * Returns the artist with the specified id, if there is an artist with this id saved in the database.
     *
     * @param id The id to look for.
     * @return Found artist with the specified id
     */
    Artist findArtistById(Long id);

    /**
     * Saves the given artist entity.
     * The entity, saved in the database, is returned - including the newly generated id.
     *
     * @param artist The artist that will be saved.
     * @return Saved artist entity.
     */
    Artist saveArtist(Artist artist);

    /**
     * Updates the given artist, if there is an artist with this id saved in the database.
     *
     * @param artist The artist that will be updated.
     */
    void updateArtist(Artist artist);

    /**
     * Deletes the given artist in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param id The id of the artist that should be saved.
     */
    void deleteArtistById(Long id);
}
