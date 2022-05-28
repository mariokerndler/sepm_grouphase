package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistRepository artistRepo;
    private final ImageFileManager ifm;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepo, ImageFileManager ifm) {
        this.ifm = ifm;
        this.artistRepo = artistRepo;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepo.findAll();
    }

    @Override
    public Artist findArtistById(Long id) {
        Optional<Artist> artist = artistRepo.findById(id);
        if (artist.isPresent()) {
            log.info(artist.toString());
            return artist.get();
        }
        throw new NotFoundException(String.format("Could not find Artist   with id %s", id));
    }

    @Override
    public Artist saveArtist(Artist artist) {
        try {
            return artistRepo.save(artist);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateArtist(Artist artist) throws IOException {
        Artist oldArtist = findArtistById(artist.getId()); {
            if (!oldArtist.getUserName().equals(artist.getUserName())) {
                ifm.renameArtistFolder(artist, oldArtist.getUserName());
            }

            if (oldArtist.getProfilePicture() != null && artist.getProfilePicture() != null) {
                if (oldArtist.getProfilePicture().getId() != artist.getProfilePicture().getId()) {
                    ifm.writeAndReplaceArtistProfileImage(artist);
                }
            }
        }

        artistRepo.save(artist);
    }
}
