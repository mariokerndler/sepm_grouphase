package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SketchRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArtworkMappingTest implements TestData {

    @Autowired
    SketchRepository sketchRepository;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private ArtworkMapper artworkMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Artwork artwork;

    private Artist artist;

    public Artwork getArtwork(Long id, byte[] content) {
        Artwork artwork = new Artwork("artwork1", "okay dog pls", "./data/image0", FileType.PNG, artistService.findArtistById(id), null, null, content);
        artworkRepository.save(artwork);
        Sketch sketch1 = Sketch.builder().id(1L).description("sketch1").imageUrl("url1").fileType(FileType.PNG).artwork(artwork).build();
        ;
        sketchRepository.save(sketch1);
        Sketch sketch2 = Sketch.builder().id(2L).description("sketch2").imageUrl("url2").fileType(FileType.GIF).artwork(artwork).build();
        sketchRepository.save(sketch2);
        Sketch sketch3 = Sketch.builder().id(3L).description("sketch3").imageUrl("url3").fileType(FileType.JPG).artwork(artwork).build();
        sketchRepository.save(sketch3);
        artwork.setSketches(List.of(sketch1, sketch2, sketch3));
        return artwork;
    }

    public Artist getTestArtist1() {
        return artist = Artist.builder()
            .userName("testArtist")
            .name("bob")
            .surname("test")
            .email("test@test.com")
            .address("test")
            .password(passwordEncoder.encode("test"))
            .admin(false)
            .userRole(UserRole.Artist)
            .description("TestDescription")
            .reviewScore(1.0)
            .build();
    }

    public ApplicationUser getTestUser() {
        return ApplicationUser.builder()
            .userName("doubleMouse09")
            .name("Henry")
            .surname("Dipper")
            .email("hd@gmx.at")
            .address("Großgmainer Hauptstraße 38")
            .password(passwordEncoder.encode("thisMyPa$$w0rd"))
            .admin(false)
            .userRole(UserRole.User)
            .build();
    }


    @Test
    @Transactional
    public void givenNothing_whenMapDetailedArtworkDtoToEntity_thenEntityHasAllProperties() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");

        ApplicationUser user = getTestUser();
        userService.registerUser(user);
        Artist artist = getTestArtist1();
        artistService.saveArtist(artist);
        Commission commission = new Commission(artist, null, user, CommissionStatus.IN_PROGRESS, 0, 0, 300, LocalDateTime.now(), LocalDateTime.now().plusHours(5), "title", "instructions", null, null, null, artwork);
        commissionService.saveCommission(commission);
        Long id = artist.getId();
        Artwork artwork = getArtwork(id, image);
        ArtworkDto artworkDto = artworkMapper.artworkToArtworkDto(artwork);
        assertAll(
            () -> assertEquals(artwork.getId(), artworkDto.getId()),
            () -> assertEquals("okay dog pls", artworkDto.getDescription()),
            () -> assertEquals("./data/image0", artworkDto.getImageUrl()),
            () -> assertEquals(FileType.PNG, artworkDto.getFileType()),
            () -> assertEquals(3, artworkDto.getSketchesDtos().size()),
            () -> assertEquals(FileType.PNG, artworkDto.getSketchesDtos().get(0).getFileType()),
            () -> assertEquals(FileType.GIF, artworkDto.getSketchesDtos().get(1).getFileType()),
            () -> assertEquals(FileType.JPG, artworkDto.getSketchesDtos().get(2).getFileType()),
            () -> assertEquals(artwork, artworkMapper.artworkDtoToArtwork(artworkDto)),
            () -> assertEquals(artwork.getCommission() == null ? null : artwork.getCommission().getId(), artworkDto.getCommissionId())
        );
    }

}
