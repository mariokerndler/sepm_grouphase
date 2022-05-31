package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface ArtworkService {

    Artwork findById(Long id);

    List<Artwork> findArtworksByArtist(Long id);

    void saveArtwork(Artwork a) throws IOException;

    void deleteArtwork(Artwork a);

    List<Artwork> searchArtworks(Specification<Artwork> spec);

    List<Artwork> searchArtworks(Specification<Artwork> spec, Pageable page, int randomSeed);
}
