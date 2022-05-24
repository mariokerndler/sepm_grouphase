package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;

import java.io.IOException;
import java.util.List;

@Service
public interface ArtworkService {
    List<Artwork> findArtworksByArtist(Long id);
    void saveArtwork(Artwork a) throws IOException;
    void deleteArtwork(Artwork a);

    List<Artwork> searchArtworks(Specification<Artwork> spec);
    List<Artwork> searchArtworks(Specification<Artwork> spec, Pageable pageable);
}
