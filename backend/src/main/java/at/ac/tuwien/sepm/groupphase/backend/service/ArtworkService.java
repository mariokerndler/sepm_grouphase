package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface ArtworkService {

    /**
     * This method returns the artwork with the specified id, if there is an artwork with this id saved in the database.
     *
     * @param id: the id to look for
     * @return the artwork with the specified id
     */
    Artwork findById(Long id);

    /**
     * This method returns all artworks by the artist with the specified id,
     * if there are artworks with by this artist saved in the database.
     *
     * @param id: the artist id to look for
     * @return a list of all artworks by the artist with the specified id
     */
    List<Artwork> findArtworksByArtist(Long id);

    /**
     * This method saves the given artwork in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param artwork: the artwork being saved
     * @return the saved entity
     * //TODO: delete throws
     */
    void saveArtwork(Artwork artwork) throws IOException;

    //TODO: update artwork

    /**
     * This method deletes the given artwork from the database.
     *
     * @param artwork: the artwork to be deleted
     */
    void deleteArtwork(Artwork artwork);

    /**
     * This method returns a list of all artworks that fit the criteria given by spec.
     *
     * @param spec: the criteria to search for
     * @return a list of fitting artworks
     */
    List<Artwork> searchArtworks(Specification<Artwork> spec);

    /**
     * This method returns a list of all artworks that fit the criteria given by spec.
     *
     * @param spec:       the criteria to search for
     * @param page:       //TODO: ask Daniel what this is for
     * @param randomSeed: a seed for randomizing the order of artworks in the list
     * @return a list of artworks
     */
    List<Artwork> searchArtworks(Specification<Artwork> spec, Pageable page, int randomSeed);
}
