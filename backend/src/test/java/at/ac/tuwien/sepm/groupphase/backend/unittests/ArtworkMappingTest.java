package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sketch;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.Enums.UserRole;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public Artwork getArtwork(Long id, byte[] content) {
        Artwork artwork = new Artwork("artwork1", "okay dog pls", "./data/image0", FileType.PNG, artistService.findArtistById(id), null, null, content);
        Sketch sketch1 = Sketch.builder().id(1L).description("sketch1").imageUrl("url1").fileType(FileType.PNG).artwork(artwork).build();
        Sketch sketch2 = Sketch.builder().id(2L).description("sketch2").imageUrl("url2").fileType(FileType.GIF).artwork(artwork).build();
        Sketch sketch3 = Sketch.builder().id(3L).description("sketch3").imageUrl("url3").fileType(FileType.JPG).artwork(artwork).build();
        artwork.setSketches(List.of(sketch1, sketch2, sketch3));
        return artwork;
    }

    public Artist getTestArtist1() {
        return artist = new Artist("testArtist", "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }


    @Test
    @Transactional
    public void givenNothing_whenMapDetailedArtworkDtoToEntity_thenEntityHasAllProperties() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");

        Artist artist = getTestArtist1();
        artistService.saveArtist(artist);
        Long id = artist.getId();
        Artwork artwork = getArtwork(id, image);
        ArtworkDto artworkDto = artworkMapper.artworkToArtworkDto(artwork);
        assertAll(
            () -> assertEquals(null, artworkDto.getId()),
            () -> assertEquals("okay dog pls", artworkDto.getDescription()),
            () -> assertEquals("./data/image0", artworkDto.getImageUrl()),
            () -> assertEquals(FileType.PNG, artworkDto.getFileType()),
            () -> assertEquals(3, artworkDto.getSketchesDtos().size()),
            () -> assertEquals(FileType.PNG, artworkDto.getSketchesDtos().get(0).getFileType()),
            () -> assertEquals(FileType.GIF, artworkDto.getSketchesDtos().get(1).getFileType()),
            () -> assertEquals(FileType.JPG, artworkDto.getSketchesDtos().get(2).getFileType())
        );
    }

}
