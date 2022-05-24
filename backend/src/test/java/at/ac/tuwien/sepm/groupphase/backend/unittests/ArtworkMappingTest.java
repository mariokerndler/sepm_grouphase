package at.ac.tuwien.sepm.groupphase.backend.unittests;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArtworkMappingTest implements TestData {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private ArtworkMapper artworkMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Artwork artwork;

    private Artist artist;

    public Artwork getArtwork(Long id) {
        return artwork = new Artwork("artwork1", "okay dog pls", "./data/image0", FileType.PNG,  artistService.findArtistById(id), null, null);
    }

    public Artist getTestArtist1() {
        return artist = new Artist("testArtist", "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }

    @Test
    public void givenNothing_whenMapDetailedArtworkDtoToEntity_thenEntityHasAllProperties() {
        // TODO: we need a photo (artwork) as Bytearray -> different dtos for posting an artwork and getting an artwork

        /*Artist artist = getTestArtist1();
        artistService.saveArtist(artist);
        Long id = artist.getId();
        Artwork artwork = getArtwork(id);
        ArtworkDto artworkDto = artworkMapper.artworkToArtworkDto(artwork);
        assertAll(
            () -> assertEquals(0, artworkDto.getId()),
            () -> assertEquals("okay dog pls", artworkDto.getDescription()),
            () -> assertEquals("./data/image0", artworkDto.getImageUrl()),
            () -> assertEquals(FileType.PNG, artworkDto.getFileType()),
            () -> assertEquals(1, artworkDto.getArtistId())
        );*/
    }

}