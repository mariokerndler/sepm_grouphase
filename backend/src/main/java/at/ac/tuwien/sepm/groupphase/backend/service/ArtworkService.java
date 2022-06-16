package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ArtworkService {

    /**
     * Returns the artwork with the specified id, if there is an artwork with this id saved in the database.
     *
     * @param id The id to look for.
     * @return The artwork with the specified id.
     */
    Artwork findById(Long id);

    /**
     * Returns all artworks by the artist with the specified id,
     * if there are artworks with this artist's id saved in the database.
     *
     * @param id the artist id to look for
     * @return a list of all artworks by the artist with the specified id
     */
    List<Artwork> findArtworksByArtist(Long id);

    /**
     * Saves the given artwork in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param artwork the artwork being saved
     */
    void saveArtwork(Artwork artwork);

    //TODO: update artwork

    /**
     * Deletes the given artwork from the database.
     *
     * @param artwork the artwork to be deleted.
     */
    void deleteArtwork(Artwork artwork);

    /**
     * Returns a list of all artworks that fit the criteria given by spec.
     *
     * @param spec The criteria to search for.
     * @return A list of fitting artworks.
     */
    List<Artwork> searchArtworks(Specification<Artwork> spec);

    /**
     * Returns a list of all artworks that fit the criteria given by spec.
     *
     * @param spec       The criteria to search for.
     * @param page       The page of artists (for performance reasons).
     * @param randomSeed A seed for randomizing the order of artworks in the list.
     * @return A list of artworks.
     */
    List<Artwork> searchArtworks(Specification<Artwork> spec, Pageable page, int randomSeed);
}
