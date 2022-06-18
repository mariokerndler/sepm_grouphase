package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    @PersistenceContext
    EntityManager em;

    private final ArtistRepository artistRepo;
    private final ImageFileManager ifm;
    private final CommissionService commissionService;
    private final ArtworkService artworkService;
    private final UserService userService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepo, ImageFileManager ifm, CommissionService commissionService, ArtworkService artworkService, UserService userService, JdbcTemplate jdbcTemplate) {
        this.ifm = ifm;
        this.artistRepo = artistRepo;
        this.commissionService = commissionService;
        this.artworkService = artworkService;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Artist> getAllArtists() {
        log.trace("calling getAllArtists() ...");
        var artists = artistRepo.findAll();
        log.info("Retrieved all artists ({})", artists.size());
        return artists;
    }

    @Override
    public Artist findArtistById(Long id) {
        log.trace("calling findArtistById() ...");
        Optional<Artist> artist = artistRepo.findById(id);
        if (artist.isPresent()) {
            log.info("Retrieved artist with id='{}'", artist.get().getId());
            return artist.get();
        }
        throw new NotFoundException(String.format("Could not find Artist with id %s", id));
    }

    // Todo: why does this return an artist?
    @Override
    public Artist saveArtist(Artist artist) {
        log.trace("calling saveArtist() ...");
        ifm.createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + artist.getUserName());

        this.userService.updateProfilePictureFiles(null, artist);

        var savedArtist = artistRepo.save(artist);
        log.info("Saved artist with id='{}'", artist.getId());
        return savedArtist;
    }

    @Override
    public void updateArtist(Artist artist) {
        log.trace("calling updateArtist() ...");
        Artist oldArtist = findArtistById(artist.getId());

        if (!oldArtist.getUserName().equals(artist.getUserName())) {
            ifm.renameArtistFolder(artist, oldArtist.getUserName());
        }

        // TODO: Expand functionality to renaming upp folder
        this.userService.updateProfilePictureFiles(oldArtist, artist);

        artistRepo.save(artist);
        log.info("Updated artist with id='{}'", artist.getId());
    }

    @Override
    public void deleteArtistById(Long id) {
        log.trace("calling deleteArtistById() ...");
        Optional<Artist> artist = artistRepo.findById(id);
        if (artist.isPresent()) {
            if (!artist.get().getCommissions().isEmpty()) {
                List<Commission> commissions = commissionService.findCommissionsByArtist(artist.get().getId());
                if (commissions != null) {
                    for (Commission c : commissions) {
                        commissionService.deleteCommission(c);
                    }
                }
            }
            if (!artist.get().getArtworks().isEmpty()) {
                List<Artwork> artworks = artworkService.findArtworksByArtist(artist.get().getId());
                if (artworks != null) {
                    for (Artwork a : artworks) {
                        artworkService.deleteArtwork(a);
                    }
                }
            }
            ifm.deleteUserProfileImage(artist.get());
            // TODO: Ifm delete files of artist
            artistRepo.delete(artist.get());
            log.info("Deleted artist with id='{}'", id);
        } else {
            throw new NotFoundException(String.format("Could not find Artist with id %s", id));
        }
    }
}
