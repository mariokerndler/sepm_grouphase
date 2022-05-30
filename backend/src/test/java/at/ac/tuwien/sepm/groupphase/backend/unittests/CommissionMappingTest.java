package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CommissionMappingTest {

    @Autowired
    private CommissionMapper commissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void givenNothing_whenMapDetailedCommissionDtoToEntity_thenEntityHasAllProperties() throws Exception {

        ApplicationUserDto userDto = new ApplicationUserDto("doubleMouse09",
            "Henry",
            "Dipper",
            "hd@gmx.at",
            "Großgmainer Hauptstraße 38",
            passwordEncoder.encode("thisMyPa$$w0rd"),
            false,
            UserRole.User);

        userDto.setId(1L);

        ArtistDto artistDto = new ArtistDto("justAnArtist",
            "Carl",
            "Zuckmayer",
            "cz@gmail.cz",
            "Zuckmayerstraße 13a",
            passwordEncoder.encode("passuwurdXC%&(5"),
            false,
            UserRole.Artist,
            3.4,
            null,
            null,
            null,
            null,
            "profSettings: colour blue, no bad reviews shown");

        artistDto.setId(2L);

        DetailedCommissionDto commissionDto = new DetailedCommissionDto(artistDto,
            userDto,
            3,
            0,
            2000.45,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24),
            "please draw me a small green snail with a cowboy's hat",
            null,
            null,
            new ReviewDto(),
            new ArtworkDto("small green snail with a cowboy's hat", "this is a tiny snail wearing a funky hat", "just/some/url", FileType.PNG, 2L));

        commissionDto.setId(1L);

        Commission commission = commissionMapper.detailedCommissionDtoToCommission(commissionDto);
        assertAll(
            () -> assertEquals(commissionDto.getId(), commission.getId()),
            () -> assertEquals(artistDto.getId(), commission.getArtist().getId()),
            () -> assertEquals(artistDto.getUserName(), commission.getArtist().getUserName()),
            () -> assertEquals(userDto.getId(), commission.getCustomer().getId()),
            () -> assertEquals(userDto.getUserName(), commission.getCustomer().getUserName()),
            () -> assertEquals(commissionDto.getSketchesShown(), commission.getSketchesShown()),
            () -> assertEquals(commissionDto.getFeedbackSent(), commission.getFeedbackSent()),
            () -> assertEquals(commissionDto.getPrice(), commission.getPrice()),
            () -> assertEquals(commissionDto.getIssueDate(), commission.getIssueDate()),
            () -> assertEquals(commissionDto.getDeadlineDate(), commission.getDeadlineDate()),
            () -> assertEquals(commissionDto.getInstructions(), commission.getInstructions()),
            () -> assertEquals((commissionDto.getReferencesDtos() == null ? null : commissionDto.getReferencesDtos().size()), (commission.getReferences() == null ? null : commission.getReferences().size())),
            () -> assertEquals((commissionDto.getReceiptsDtos() == null ? null : commissionDto.getReceiptsDtos().size()), (commission.getReceipts() == null ? null : commission.getReceipts().size())),
            () -> assertEquals((commissionDto.getReviewDto() == null ? null : commissionDto.getReviewDto().getId()), (commission.getReview() == null ? null : commission.getReview().getId())),
            () -> assertEquals((commissionDto.getArtworkDto() == null ? null : commissionDto.getArtworkDto().getId()), (commission.getArtwork() == null ? null : commission.getArtwork().getId()))
        );
    }

}

