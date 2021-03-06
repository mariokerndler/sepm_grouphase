package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void givenNothing_whenMapDetailedArtistDtoToEntity_thenEntityHasAllProperties() throws Exception {
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
