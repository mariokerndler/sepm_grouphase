package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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


        Commission commission = Commission.builder()
            .artist()
            .customer()
            .sketchesShown()
            .feedbackSent()
            .price()
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