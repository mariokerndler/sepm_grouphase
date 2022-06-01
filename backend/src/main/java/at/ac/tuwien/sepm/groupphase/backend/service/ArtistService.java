package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    /**
     * This method returns all artists saved in the database.
     *
     * @return a list of all artists
     */
    List<Artist> getAllArtists();

    /**
     * This method returns the artist with the specified id, if there is an artist with this id saved in the database.
     *
     * @param id: the id to look for
     * @return the artist with the specified id
     */
    Artist findArtistById(Long id);

    /**
     * This method saves the given artist in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param artist: the artist being saved
     * @return the saved entity
     */
    Artist saveArtist(Artist artist);

    /**
     * This method updates the given artist, if there is an artist with this id saved in the database.
     *
     * @param artist: the artist being updated
     */
    void updateArtist(Artist artist) throws IOException;

    /**
     * This method deletes the given artist in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param id: the artist being saved
     * @return the saved entity
     */
    void deleteArtistById(Long id);
}
