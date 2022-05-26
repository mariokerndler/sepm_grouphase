package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommissionDto {

    private long id;
    private long artistId;
    private long customerId;
    private int sketchesShown;
    private int feedbackSent;
    private double price;
    private LocalDateTime issueDate;
    private LocalDateTime deadlineDate;
    private String instructions;
    //TODO: can we map this to show just the url?
    private List<Reference> references;
    //TODO: what about this?
    private List<Receipt> receipts;
    //TODO: and this?
    private Review review;
    private ArtworkDto artworkDto;

    //TODO: generate constructor after deciding on references, receipts and reviews

    //TODO: generate toString after deciding on references, receipts and reviews

}
