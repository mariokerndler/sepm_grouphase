package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistRepository artistRepo;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepo) {
        this.artistRepo = artistRepo;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepo.findAll();
    }

    @Override
    public Artist findArtistById(Long id) {
        Optional<Artist> artist= artistRepo.findById(id);
        if(artist.isPresent()){
            return artist.get();
        }
        throw new NotFoundException(String.format("Could not find Artist with id %s", id));
    }

    @Override
    public Artist saveArtist(Artist artist) {
        return artistRepo.save(artist);
    }
}
