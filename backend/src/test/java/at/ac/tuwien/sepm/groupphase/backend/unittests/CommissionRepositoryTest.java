package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class CommissionRepositoryTest {

    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ArtistRepository artistRepository;

    @AfterEach
    public void beforeEach() {
        commissionRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void givenNothing_whenSaveCommission_thenFindListWithOneElementAndFindCommissionById() {

        Artist artist = Artist.builder()
            .userName("momo45")
            .name("Millie")
            .surname("Bobbington")
            .email("mil.b@aol.de")
            .address("Mispelstreet")
            .password("passwordhash")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("Description of new artist")
            .profileSettings("settings string")
            .build();

        ApplicationUser user = ApplicationUser.builder()
            .userName("sunscreen98")
            .name("Mike")
            .surname("Regen")
            .email("mikey98@gmail.com")
            .address("Greatstreet")
            .password("passwordhash2")
            .admin(false)
            .userRole(UserRole.User)
            .build();

        artistRepository.save(artist);
        userRepository.save(user);

        Commission commission = Commission.builder()
            .artist(artist)
            .customer(user)
            .sketchesShown(3)
            .feedbackSent(0)
            .price(300)
            .issueDate(LocalDateTime.now())
            .deadlineDate(LocalDateTime.now().plusDays(20))
            .instructions("do the thing")
            .build();

        commissionRepository.save(commission);

        assertAll(
            () -> assertEquals(1, commissionRepository.findAll().size()),
            () -> assertNotNull(commissionRepository.findById(commission.getId()))
        );
    }

    @Test
    public void givenNothing_saveCommission_andCheckIfPresent_thenDeleteCommission_andCheckIfGone() {
        Artist artist = Artist.builder()
            .userName("muRi77")
            .name("Murray")
            .surname("Richards")
            .email("mur@aol.de")
            .address("Mispelstreet")
            .password("passwordhash")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("Description of new artist")
            .profileSettings("settings string")
            .build();

        ApplicationUser user = ApplicationUser.builder()
            .userName("sunnyboy56")
            .name("Leslie")
            .surname("Rogers")
            .email("lezzy@gmail.com")
            .address("Greatstreet")
            .password("passwordhash2")
            .admin(false)
            .userRole(UserRole.User)
            .build();

        artistRepository.save(artist);
        userRepository.save(user);

        Commission commission = Commission.builder()
            .artist(artist)
            .customer(user)
            .sketchesShown(3)
            .feedbackSent(0)
            .price(300)
            .issueDate(LocalDateTime.now())
            .deadlineDate(LocalDateTime.now().plusWeeks(18))
            .instructions("make me a statue")
            .build();

        commissionRepository.save(commission);

        assertAll(
            () -> assertEquals(1, commissionRepository.findAll().size()),
            () -> assertNotNull(commissionRepository.findById(commission.getId()))
        );

        commissionRepository.deleteById(commission.getId());

        assertAll(
            () -> assertEquals(0, commissionRepository.findAll().size()),
            () -> assertTrue(commissionRepository.findById(commission.getId()).isEmpty())
        );
    }


    @Test
    public void givenNothing_saveCommission_andCheckIfPresent_thenUpdate_andCheckContents() {
        Artist artist = Artist.builder()
            .userName("cliff2005")
            .name("Cliff")
            .surname("Bar")
            .email("c.b@aol.de")
            .address("Mispelstreet")
            .password("passwordhash")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("Description of new artist")
            .profileSettings("settings string")
            .build();

        ApplicationUser user = ApplicationUser.builder()
            .userName("jumboJames2")
            .name("Jeanie")
            .surname("James")
            .email("lezzy@gmail.com")
            .address("Greatstreet")
            .password("passwordhash2")
            .admin(false)
            .userRole(UserRole.User)
            .build();

        artistRepository.save(artist);
        userRepository.save(user);

        Commission commission = Commission.builder()
            .artist(artist)
            .customer(user)
            .sketchesShown(4)
            .feedbackSent(0)
            .price(8000)
            .issueDate(LocalDateTime.now())
            .deadlineDate(LocalDateTime.now().plusWeeks(12))
            .instructions("draw me like one of your french girls")
            .build();

        commissionRepository.save(commission);

        assertAll(
            () -> assertEquals(1, commissionRepository.findAll().size()),
            () -> assertEquals(false, commissionRepository.findById(commission.getId()).isEmpty())
        );

        commission.setFeedbackSent(4);

        commissionRepository.save(commission);

        assertAll(
            () -> assertEquals(1, commissionRepository.findAll().size()),
            () -> assertEquals(4, commissionRepository.findById(commission.getId()).get().getFeedbackSent())
        );
    }
}