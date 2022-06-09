package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.Enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;


    public Artist getTestArtist1() {
        return Artist.builder()
            .userName("testArtist")
            .name("bob")
            .surname("test")
            .email("test@test.com")
            .address("test")
            .password("test")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("TestDescription")
            .reviewScore(1.0)
            .build();
    }


    @Test
    public void givenNothing_whenSaveArtist_thenFindListWithOneElementAndArtistById() {
        Artist artist = getTestArtist1();
        artistRepository.save(artist);

        assertAll(
            () -> assertEquals(1, artistRepository.findAll().size()),
            () -> assertNotNull(artistRepository.findById(artist.getId()))
        );
    }
}
