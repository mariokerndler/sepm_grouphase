package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProfilePictureDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ProfilePictureMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.utils.Enums.UserRole;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProfilePictureMappingTest implements TestData {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProfilePictureMapper profilePictureMapper;

    private Artist artist;

    @BeforeEach
    public void beforeEach() {
        artistRepository.deleteAll();

        this.artist = Artist.builder()
            .userName("justAnArtist")
            .name("Carl")
            .surname("Zuckmayer")
            .email("cz@gmail.cz")
            .address("Zuckmayerstraße 13a")
            .password("passuwurdXC%&(5")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("Description of new artist")
            .profileSettings("profSettings: colour blue, no bad reviews shown")
            .build();

        artistRepository.save(artist);
    }

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
    public void givenSavedArtist_whenMapProfilePictureDtoToEntity_thenEntityHasAllProperties() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes(IMAGE_URLS[0]);

        ProfilePictureDto profilePictureDto = getProfilePictureDto(artist.getId(), image);
        ProfilePicture profilePicture = profilePictureMapper.profilePictureDtoToProfilePicture(profilePictureDto);
        assertAll(
            () -> assertEquals(null, profilePicture.getId()),
            () -> assertEquals("./data/image0", profilePicture.getImageUrl()),
            () -> assertTrue(Arrays.equals(image, profilePicture.getImageData())),
            () -> assertEquals(FileType.PNG, profilePicture.getFileType()),
            () -> assertEquals(artist.getId(), profilePicture.getArtist() == null ? null : profilePicture.getArtist().getId())
        );
    }

    @Test
    @Transactional
    public void givenSavedArtist_whenMapProfilePictureDtoToEntityWithoutArtist_thenEntityHasAllPropertiesExceptArtistId() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes(IMAGE_URLS[0]);

        ProfilePictureDto profilePictureDto = getProfilePictureDto(artist.getId(), image);
        ProfilePicture profilePicture = profilePictureMapper.profilePictureDtoWithoutArtistIdToProfilePicture(profilePictureDto);
        assertAll(
            () -> assertEquals(null, profilePicture.getId()),
            () -> assertEquals("./data/image0", profilePicture.getImageUrl()),
            () -> assertTrue(Arrays.equals(image, profilePicture.getImageData())),
            () -> assertEquals(FileType.PNG, profilePicture.getFileType()),
            () -> assertEquals(null, profilePicture.getArtist() == null ? null : profilePicture.getArtist().getId())
        );
    }

    @Test
    @Transactional
    public void givenSavedArtist_whenMapProfilePictureToDto_thenDtoHasAllProperties() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes(IMAGE_URLS[0]);

        ProfilePicture profilePicture = getProfilePicture(artist, image);
        ProfilePictureDto profilePictureDto = profilePictureMapper.profilePictureToProfilePictureDto(profilePicture);
        assertAll(
            () -> assertEquals(null, profilePictureDto.getId()),
            () -> assertEquals("./data/image0", profilePictureDto.getImageUrl()),
            () -> assertTrue(Arrays.equals(image, profilePictureDto.getImageData())),
            () -> assertEquals(FileType.PNG, profilePictureDto.getFileType()),
            () -> assertEquals(artist.getId(), profilePictureDto.getArtistId())
        );
    }

}
