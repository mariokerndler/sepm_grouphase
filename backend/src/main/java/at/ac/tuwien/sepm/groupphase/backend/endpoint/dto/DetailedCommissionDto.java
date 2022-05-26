package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetailedCommissionDto {

    private Long id;
    private ArtistDto artistDto;
    private ApplicationUserDto customerDto;
    private int sketchesShown;
    private int feedbackSent;
    private double price;
    private LocalDateTime issueDate;
    private LocalDateTime deadlineDate;
    private String instructions;
    private List<ReferenceDto> references;
    private List<ReceiptDto> receipts;
    private ReviewDto review;
    private ArtworkDto artworkDto;

    public DetailedCommissionDto(ArtistDto artistDto, ApplicationUserDto customerDto, int sketchesShown,
                                 int feedbackSent, double price, LocalDateTime issueDate, LocalDateTime deadlineDate,
                                 String instructions, List<ReferenceDto> references, List<ReceiptDto> receipts,
                                 ReviewDto review, ArtworkDto artworkDto) {
        this.artistDto = artistDto;
        this.customerDto = customerDto;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.instructions = instructions;
        this.references = references;
        this.receipts = receipts;
        this.review = review;
        this.artworkDto = artworkDto;
    }

    @Override
    public String toString() {
        return "DetailedCommissionDto{" +
            "id=" + id +
            ", artistDto=" + artistDto.getId() +
            ", customerDto=" + customerDto.getId() +
            ", sketchesShown=" + sketchesShown +
            ", feedbackSent=" + feedbackSent +
            ", price=" + price +
            ", issueDate=" + issueDate +
            ", deadlineDate=" + deadlineDate +
            ", instructions='" + instructions + '\'' +
            (references == null ? "" : ", references=" + references.stream().map(ReferenceDto::getId).toList()) +
            (references == null ? "" : ", receipts=" + receipts.stream().map(ReceiptDto::getId).toList()) +
            ", review=" + review +
            (artworkDto == null ? "" : ", artworkDto=" + artworkDto.getId()) +
            '}';
    }
}
