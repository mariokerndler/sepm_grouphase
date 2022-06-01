package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    CommissionRepository commissionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    ReviewRepository reviewRepository;

    private Artist artist;
    private ApplicationUser user;
    private Artwork artwork;
    private Commission commission;
    private Review review;

    @BeforeEach
    public void beforeEach() {
        artworkRepository.deleteAll();
        commissionRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();

        this.user = ApplicationUser.builder()
            .userName("doubleMouse09")
            .name("Henry")
            .surname("Dipper")
            .email("hd@gmx.at")
            .address("Großgmainer Hauptstraße 38")
            .password(passwordEncoder.encode("thisMyPa$$w0rd"))
            .admin(false)
            .userRole(UserRole.User)
            .build();

        this.artist = Artist.builder()
            .userName("justAnArtist")
            .name("Carl")
            .surname("Zuckmayer")
            .email("cz@gmail.cz")
            .address("Zuckmayerstraße 13a")
            .password("passuwurdXC%&(5")
            .admin(false)
            .userRole(UserRole.Artist)
            .description("Description of new artist")
            .profileSettings("profSettings: colour blue, no bad reviews shown")
            .build();


        userRepository.save(user);
        artistRepository.save(artist);


        this.commission = Commission.builder()
            .artist(artist)
            .customer(user)
            .title("Cowboy Snail")
            .sketchesShown(3)
            .feedbackSent(0)
            .price(2000.45)
            .issueDate(LocalDateTime.now())
            .deadlineDate(LocalDateTime.now().plusHours(24))
            .instructions("please draw me a small green snail with a cowboy's hat")
            .build();

        commissionRepository.save(commission);

        this.artwork = Artwork.builder()
            .name("small green snail with a cowboy's hat")
            .description("this is a tiny snail wearing a funky hat")
            .imageUrl("just/some/url")
            .fileType(FileType.PNG)
            .artist(artist)
            .commission(commission)
            .build();

        artworkRepository.save(artwork);


        this.review = Review.builder()
            .artist(artist)
            .customer(user)
            .text("I really enjoyed working with Carl. He drew me a nice smol snail:)")
            .commission(commission)
            .starRating(5)
            .build();

        reviewRepository.save(review);


    }


    @Test
    @Transactional
    public void givenNothing_whenMapDetailedCommissionDtoToEntity_thenEntityHasAllProperties() {

        ApplicationUserDto userDto = new ApplicationUserDto("doubleMouse09",
            "Henry",
            "Dipper",
            "hd@gmx.at",
            "Großgmainer Hauptstraße 38",
            passwordEncoder.encode("thisMyPa$$w0rd"),
            false,
            UserRole.User);

        userDto.setId(user.getId());

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

        artistDto.setId(artist.getId());

        ReviewDto reviewDto = new ReviewDto(artistDto, userDto, "I really enjoyed working with Carl. He drew me a nice smol snail:)", null, 5);
        ArtworkDto artworkDto = new ArtworkDto("small green snail with a cowboy's hat", "this is a tiny snail wearing a funky hat", "just/some/url", FileType.PNG, artist.getId());


        DetailedCommissionDto commissionDto = new DetailedCommissionDto(artistDto,
            userDto,
            3,
            0,
            2000.45,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24),
            "Commission Title",
            "please draw me a small green snail with a cowboy's hat",
            null,
            null,
            reviewDto,
            artworkDto
        );

        commissionDto.setId(commission.getId());
        reviewDto.setCommissionId(commission.getId());

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


    @Test
    @Transactional
    public void whenMapEntityToDetailedCommissionDto_thenDetailedCommissionDtoHasAllProperties() {

        DetailedCommissionDto commissionDto = commissionMapper.commissionToDetailedCommissionDto(this.commission);
        assertAll(
            () -> assertEquals(commission.getId(), commissionDto.getId()),
            () -> assertEquals(artist.getId(), commissionDto.getArtistDto().getId()),
            () -> assertEquals(artist.getUserName(), commissionDto.getArtistDto().getUserName()),
            () -> assertEquals(user.getId(), commissionDto.getCustomerDto().getId()),
            () -> assertEquals(user.getUserName(), commissionDto.getCustomerDto().getUserName()),
            () -> assertEquals(commission.getSketchesShown(), commissionDto.getSketchesShown()),
            () -> assertEquals(commission.getFeedbackSent(), commissionDto.getFeedbackSent()),
            () -> assertEquals(commission.getPrice(), commissionDto.getPrice()),
            () -> assertEquals(commission.getIssueDate(), commissionDto.getIssueDate()),
            () -> assertEquals(commission.getDeadlineDate(), commissionDto.getDeadlineDate()),
            () -> assertEquals(commission.getInstructions(), commissionDto.getInstructions()),
            () -> assertEquals((commission.getReferences() == null ? null : commission.getReferences().size()), (commissionDto.getReferencesDtos() == null ? null : commissionDto.getReferencesDtos().size())),
            () -> assertEquals((commission.getReceipts() == null ? null : commission.getReceipts().size()), (commissionDto.getReceiptsDtos() == null ? null : commissionDto.getReceiptsDtos().size())),
            () -> assertEquals((commission.getReview() == null ? null : commission.getReview().getId()), (commissionDto.getReviewDto() == null ? null : commissionDto.getReviewDto().getId())),
            () -> assertEquals((commission.getArtwork() == null ? null : commission.getArtwork().getId()), (commissionDto.getArtworkDto() == null ? null : commissionDto.getArtworkDto().getId()))
        );
    }
}

