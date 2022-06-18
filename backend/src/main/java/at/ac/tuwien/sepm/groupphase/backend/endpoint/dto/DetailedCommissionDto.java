package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetailedCommissionDto {

    private Long id;

    @Valid
    private ArtistDto artistDto;

    private List<@Valid ArtistDto> artistCandidatesDtos;

    @NotNull
    @Valid
    private ApplicationUserDto customerDto;

    @NotNull
    private CommissionStatus status;

    @Min(0)
    private int sketchesShown;

    @Min(0)
    private int feedbackSent;

    @Min(0)
    private double price;

    @Min(0)
    private int feedbackRounds;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadlineDate;

    @Size(max = 50)
    @ValidAlphaNumericWithSpaces
    private String title;

    @Size(max = 255)
    private String instructions;

    private List<@Valid ReferenceDto> referencesDtos;

    private List<@Valid ReceiptDto> receiptsDtos;

    @Valid
    private ReviewDto reviewDto;

    @Valid
    private ArtworkDto artworkDto;

    public DetailedCommissionDto(ArtistDto artistDto,
                                 List<ArtistDto> artistCandidatesDtos,
                                 ApplicationUserDto customerDto,
                                 CommissionStatus status,
                                 int sketchesShown,
                                 int feedbackSent,
                                 double price,
                                 LocalDateTime issueDate,
                                 LocalDateTime deadlineDate,
                                 String title,
                                 String instructions,
                                 List<ReferenceDto> referencesDtos,
                                 List<ReceiptDto> receiptsDtos,
                                 ReviewDto reviewDto,
                                 ArtworkDto artworkDto) {
        this.artistDto = artistDto;
        this.artistCandidatesDtos = artistCandidatesDtos;
        this.customerDto = customerDto;
        this.status = status;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.title = title;
        this.instructions = instructions;
        this.referencesDtos = referencesDtos;
        this.receiptsDtos = receiptsDtos;
        this.reviewDto = reviewDto;
        this.artworkDto = artworkDto;
    }

    @Override
    public String toString() {
        return "DetailedCommissionDto{"
            + "id=" + id
            + ", artistDtoId=" + (artistDto == null ? null : artistDto.getId())
            + ", artistCandidatesDtosIds=" + (artistCandidatesDtos == null ? null : artistCandidatesDtos.stream().map(ArtistDto::getId).toList())
            + ", customerDtoId=" + (customerDto == null ? null : customerDto.getId())
            + ", status=" + status
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", deadlineDate=" + deadlineDate
            + ", title=" + title
            + ", instructions='" + instructions + '\''
            + ", referencesDtosIds=" + (referencesDtos == null ? null : referencesDtos.stream().map(ReferenceDto::getId).toList())
            + ", receiptsDtosIds=" + (receiptsDtos == null ? null : receiptsDtos.stream().map(ReceiptDto::getId).toList())
            + ", reviewDto=" + reviewDto
            + ", artworkDtoId=" + (artworkDto == null ? null : artworkDto.getId()) + '}';
    }
}
