package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArtistMappingTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Artist artist;

    public Artist getTestArtist1() {
        return artist = new Artist("testArtist", "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }

    @Test
    public void givenNothing_whenMapDetailedArtworkDtoToEntity_thenEntityHasAllProperties() throws Exception {
        Artist artist = getTestArtist1();
        ArtistDto artistDto = artistMapper.artistToArtistDto(artist);

        assertAll(
            () -> assertEquals(null, artistDto.getId()),
            () -> assertEquals("testArtist", artistDto.getUserName()),
            () -> assertEquals("test", artistDto.getSurname()),
            () -> assertEquals(UserRole.Artist, artistDto.getUserRole()),
            () -> assertEquals(1.0, artistDto.getReviewScore())
        );

    }

}
