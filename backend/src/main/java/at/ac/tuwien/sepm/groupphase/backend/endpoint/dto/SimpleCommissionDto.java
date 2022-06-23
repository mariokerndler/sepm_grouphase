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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class SimpleCommissionDto {

    private Long id;

    private Long artistId;

    @NotNull(message = CommissionValidationMessages.CUSTOMER_IS_NULL)
    private Long customerId;

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

    //@Future(message = CommissionValidationMessages.DEADLINE_DATE_NOT_FUTURE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadlineDate;

    @Size(max = 100, message = CommissionValidationMessages.TILE_LENGTH_TOO_LONG)
    @ValidAlphaNumericWithSpaces(message = CommissionValidationMessages.TITLE_NON_ALPHA_NUMERIC_SPACES)
    private String title;

    @Size(max = 512, message = CommissionValidationMessages.INSTRUCTIONS_TOO_LONG)
    private String instructions;

    private List<Long> referencesIds;

    private Long artworkId;

    public SimpleCommissionDto(Long artistId, Long customerId, CommissionStatus status, int sketchesShown, int feedbackSent,
                               double price, LocalDateTime issueDate, LocalDateTime deadlineDate, String title,
                               String instructions, List<Long> referencesIds, Long artworkId) {
        this.artistId = artistId;
        this.customerId = customerId;
        this.status = status;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.title = title;
        this.instructions = instructions;
        this.referencesIds = referencesIds;
        this.artworkId = artworkId;
    }

    @Override
    public String toString() {
        return "SimpleCommissionDto{"
            + "id=" + id
            + ", artistId=" + artistId
            + ", customerId=" + customerId
            + ", status=" + status
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", deadlineDate=" + deadlineDate
            + ", title=" + title
            + ", instructions='" + instructions + '\''
            + ", referenceIds=" + referencesIds
            + ", artworkId=" + artworkId + '}';
    }
}
