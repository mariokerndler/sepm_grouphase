package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepo;
    private final ImageFileManager ifm;

    @Autowired
    public ArtworkServiceImpl(ArtworkRepository artworkRepo, ImageFileManager ifm) {
        this.artworkRepo = artworkRepo;
        this.ifm = ifm;
    }

    @Override
    public List<Artwork> findArtworksByArtist(Long id) {
        log.trace("calling findArtworksByArtist() ...");
        var foundArtworks = artworkRepo.findArtworkByArtistId(id);
        log.info("Retrieved all artworks by artist with id='{}'", id);
        return foundArtworks;
    }

    @Override
    public Artwork findById(Long id) {
        log.trace("calling findById() ...");
        Optional<Artwork> artwork = artworkRepo.findById(id);

        if (artwork.isPresent()) {
            log.info("Retrieved artwork with id='{}'", id);
            return artwork.get();
        } else {
            throw new NotFoundException(String.format("Could not find artwork with id %s", id));
        }
    }

    @Override
    public void saveArtwork(Artwork a) {
        log.trace("calling saveArtwork() ...");
        a.setImageUrl(this.ifm.writeArtistImage(a));
        this.artworkRepo.save(a);
        log.info("Saved artwork with id='{}'", a.getId());
    }

    @Override
    public void deleteArtwork(Artwork a) {
        log.trace("calling deleteArtwork() ...");
        try {
            this.artworkRepo.deleteById(a.getId());
            this.ifm.deleteArtistImage(a);
            log.info("Deleted artwork with id='{}'", a.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Could not find artwork with id %s", a.getId()));
        }
    }

    @Override
    public List<Artwork> searchArtworks(Specification<Artwork> spec) {
        log.trace("calling searchArtworks() ...");
        List<Artwork> foundArtworks = artworkRepo.findAll(spec);
        log.info("Retrieved all artworks {} ({})",
            "matching the search request: " + spec.toString(),
            foundArtworks.size());
        return foundArtworks;
    }

    @Override
    public List<Artwork> searchArtworks(Specification<Artwork> spec, Pageable page, int randomSeed) {
        log.trace("calling searchArtworks() ...");
        List<Artwork> foundArtworks;
        if (spec == null) {
            if (randomSeed != 0) {
                foundArtworks = this.artworkRepo.findArtworkRandom(randomSeed, page).getContent();
                log.info("Retrieved all artworks with page='{}' and randomSeed='{}'", page, randomSeed);
            } else {
                foundArtworks = this.artworkRepo.findAll(page).getContent();
                log.info("Retrieved all artworks with page='{}'", page);
            }
            return foundArtworks;
        }

        foundArtworks = this.artworkRepo.findAll(spec, page).getContent();
        log.info("Retrieved all artworks {} and page='{}' ({})",
            "matching the search request: " + spec.toString(),
            page,
            foundArtworks.size());
        return foundArtworks;
    }
}
