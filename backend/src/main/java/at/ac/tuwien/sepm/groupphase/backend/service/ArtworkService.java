package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtworkService {
    List<Artwork> findArtworksByArtist(Long id);
    void saveArtwork(Artwork a);
    void deleteArtwork(Long id);
}
