package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCommissionDto {

    private Long id;

    private Long artistId;

    @NotNull
    private Long customerId;

    @Min(0)
    private int sketchesShown;

    @Min(0)
    private int feedbackSent;

    @Min(0)
    private double price;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadlineDate;

    private String instructions;

    //TODO: can we map this to show just the url? ask in meeting
    private List<Long> referencesIds;

    public SimpleCommissionDto(Long artistId, Long customerId, int sketchesShown, int feedbackSent,
                               double price, LocalDateTime issueDate, LocalDateTime deadlineDate,
                               String instructions, List<Long> referencesIds) {
        this.artistId = artistId;
        this.customerId = customerId;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.instructions = instructions;
        this.referencesIds = referencesIds;
    }

    @Override
    public String toString() {
        return "SimpleCommissionDto{"
            + "id=" + id
            + ", artistId=" + artistId
            + ", customerId=" + customerId
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", deadlineDate=" + deadlineDate
            + ", instructions='" + instructions + '\''
            + ", referenceIds=" + referencesIds + '}';
    }
}