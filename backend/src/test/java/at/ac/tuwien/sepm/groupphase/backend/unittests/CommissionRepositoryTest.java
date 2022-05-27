package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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


    @Test
    public void givenNothing_whenSaveCommission_thenFindListWithOneElementAndFindCommissionById() {
        ApplicationUser user1 = ApplicationUser.builder()
            .userName("momo45")
            .name("Millie")
            .surname("Bobbington")
            .email("mil.b@aol.de")
            .address("Mispelstreet")
            .password("passwordhash")
            .admin(false)
            .userRole(UserRole.Artist)
            .build();

        ApplicationUser user2 = ApplicationUser.builder()
            .userName("sunscreen98")
            .name("Mike")
            .surname("Regen")
            .email("mikey98@gmail.com")
            .address("Greatstreet")
            .password("passwordhash2")
            .admin(false)
            .userRole(UserRole.User)
            .build();

        //TODO: how to build subclass thing?
        Artist artist = Artist.builder()
            .profilePicture()
            .description()
            .profileSettings()
            .reviewScore()
            .gallery()
            .artworks()
            .commissions()
            .reviews()
            .tags()
            .build();

        Commission commission = Commission.builder()
            .artist(artist)
            .customer(user2)
            .sketchesShown(3)
            .feedbackSent(0)
            .price(300)
            .issueDate()
            .deadlineDate()
            .instructions()
            .references()
            .receipts()
            .review()
            .artwork()
            .build();

        commissionRepository.save(commission);

        assertAll(
            () -> assertEquals(1, commissionRepository.findAll().size()),
            () -> assertNotNull(commissionRepository.findById(commission.getId()))
        );
    }
}