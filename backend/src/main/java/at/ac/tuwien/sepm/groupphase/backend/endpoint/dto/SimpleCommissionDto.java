package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCommissionDto {

    private Long id;
    private Long artistId;
    private Long customerId;
    private int sketchesShown;
    private int feedbackSent;
    private double price;
    private LocalDateTime issueDate;
    private LocalDateTime deadlineDate;
    private String instructions;
    //TODO: can we map this to show just the url?
    private List<Long> referenceIds;
    private List<Long> receiptIds;

    //TODO: generate constructor after deciding on references, receipts and reviews

    //TODO: generate toString after deciding on references, receipts and reviews

}
