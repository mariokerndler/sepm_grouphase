package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProfilePictureDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ProfilePictureMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProfilePictureMappingTest implements TestData {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ProfilePictureMapper profilePictureMapper;

    public ProfilePicture getProfilePicture(Artist artist, byte[] imageData) {
        return ProfilePicture.builder()
            .imageUrl("./data/image0")
            .imageData(imageData)
            .fileType(FileType.PNG)
            .artist(artist)
            .build();
    }

    public ProfilePictureDto getProfilePictureDto(Long artistId, byte[] imageData) {
        ProfilePictureDto profilePictureDto = new ProfilePictureDto();
        profilePictureDto.setImageUrl("./data/image0");
        profilePictureDto.setImageData(imageData);
        profilePictureDto.setFileType(FileType.PNG);
        profilePictureDto.setArtistId(artistId);
        return profilePictureDto;
    }


    @Test
    @Transactional
    public void givenNothing_whenMapProfilePictureDtoToEntity_thenEntityHasAllProperties() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes(IMAGE_URLS[0]);

        Artist artist = getTestArtist1();
        artistService.saveArtist(artist);
        Long id = artist.getId();
        Artwork artwork = getArtwork(id, image);
        ArtworkDto artworkDto = artworkMapper.artworkToArtworkDto(artwork);
        assertAll(
            () -> assertEquals(null, artworkDto.getId()),
            () -> assertEquals("okay dog pls", artworkDto.getDescription()),
            () -> assertEquals("./data/image0", artworkDto.getImageUrl()),
            () -> assertEquals(FileType.PNG, artworkDto.getFileType())
        );
    }

}
