package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Artist getTestArtist1() {
        return new Artist("testArtist", "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }

    @Test
    public void givenNothing_whenSaveMessage_thenFindListWithOneElementAndFindMessageById() {
        Artist artist = getTestArtist1();
        artistRepository.save(artist);

        assertAll(
            () -> assertEquals(1, artistRepository.findAll().size()),
            () -> assertNotNull(artistRepository.findById(artist.getId()))
        );
    }
}
