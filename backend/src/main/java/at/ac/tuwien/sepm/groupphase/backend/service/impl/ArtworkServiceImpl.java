package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
@Slf4j
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepo;

    @Autowired
    public ArtworkServiceImpl(ArtworkRepository artworkRepo) {
        this.artworkRepo = artworkRepo;
    }

    @Override
    public List<Artwork> findArtworksByArtist(Long id) {
        return artworkRepo.findArtworkByArtistId(id);
    }

    @Override
    public void saveArtwork(Artwork a) {
        this.artworkRepo.save(a);
    }

    @Override
    public void deleteArtwork(Long id) {
        this.artworkRepo.deleteById(id);
    }

    @Override
    public List<Artwork> searchArtworks(Specification<Artwork> spec) {
        return artworkRepo.findAll(spec);
    }
}
