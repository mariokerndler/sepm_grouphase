package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepo;
    private final ImageFileManager ifm;
    private final CommissionService commissionService;
    private final ArtworkService artworkService;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepo, ImageFileManager ifm, CommissionService commissionService, ArtworkService artworkService) {
        this.ifm = ifm;
        this.artistRepo = artistRepo;
        this.commissionService = commissionService;
        this.artworkService = artworkService;
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
        throw new NotFoundException(String.format("Could not find Artist with id %s", id));
    }

    // Todo: why does this return an artist?
    @Override
    public Artist saveArtist(Artist artist) {

        ifm.createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + artist.getUserName());

        if (artist.getProfilePicture() == null) {
            artist.setProfilePicture(ProfilePicture.getDefaultProfilePicture(artist));
        } else {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(artist);
            artist.getProfilePicture().setImageUrl(imageUrl);
        }
        return artistRepo.save(artist);
    }

    @Override
    public void updateArtist(Artist artist) {
        Artist oldArtist = findArtistById(artist.getId());

        if (!oldArtist.getUserName().equals(artist.getUserName())) {
            ifm.renameArtistFolder(artist, oldArtist.getUserName());
        }

        // TODO: What to do when user deletes profile picture ? Choose avatar to default back to
        if (artist.getProfilePicture() == null) {
            artist.setProfilePicture(ProfilePicture.getDefaultProfilePicture(artist));
        } else if ((oldArtist.getProfilePicture() == null) || oldArtist.getProfilePicture().getId() != artist.getProfilePicture().getId()) {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(artist);
            artist.getProfilePicture().setImageUrl(imageUrl);
        }

        artistRepo.save(artist);
    }

    @Override
    public void deleteArtistById(Long id) {
        Optional<Artist> artist = artistRepo.findById(id);
        if (artist.isPresent()) {
            log.info(artist.toString());
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
            // TODO: Ifm delete files of artist
            artistRepo.delete(artist.get());
        } else {
            throw new NotFoundException(String.format("Could not find Artist with id %s", id));
        }
    }

}
