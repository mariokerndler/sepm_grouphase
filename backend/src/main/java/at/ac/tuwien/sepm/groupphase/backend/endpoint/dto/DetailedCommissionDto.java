package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.CommissionValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Validated
@NoArgsConstructor
public class DetailedCommissionDto {

    private Long id;

    @Valid
    private ArtistDto artistDto;

    private List<@Valid ArtistDto> artistCandidatesDtos;

    @NotNull(message = CommissionValidationMessages.CUSTOMER_IS_NULL)
    @Valid
    private ApplicationUserDto customerDto;

    @NotNull(message = CommissionValidationMessages.COMMISSION_STATUS_IS_NULL)
    private CommissionStatus status;

    @Min(value = 0, message = CommissionValidationMessages.SKETCHES_SHOWN_NEGATIVE)
    private int sketchesShown;

    @Min(value = 0, message = CommissionValidationMessages.FEEDBACK_SENT_NEGATIVE)
    private int feedbackSent;

    @Min(value = 0, message = CommissionValidationMessages.PRICE_NEGATIVE)
    private double price;

    @Min(value = 0, message = CommissionValidationMessages.FEEDBACK_ROUNDS_NEGATIVE)
    private int feedbackRounds;

    @PastOrPresent(message = CommissionValidationMessages.ISSUE_DATE_NOT_PAST_PRESENT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;

    @Future(message = CommissionValidationMessages.DEADLINE_DATE_NOT_FUTURE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadlineDate;

    @Size(max = 100, message = CommissionValidationMessages.TILE_LENGTH_TOO_LONG)
    @ValidAlphaNumericWithSpaces(message = CommissionValidationMessages.TITLE_NON_ALPHA_NUMERIC_SPACES)
    private String title;

    @Size(max = 512, message = CommissionValidationMessages.INSTRUCTIONS_TOO_LONG)
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
