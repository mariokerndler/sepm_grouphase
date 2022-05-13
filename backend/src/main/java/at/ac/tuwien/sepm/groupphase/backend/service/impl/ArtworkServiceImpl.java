package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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
}
