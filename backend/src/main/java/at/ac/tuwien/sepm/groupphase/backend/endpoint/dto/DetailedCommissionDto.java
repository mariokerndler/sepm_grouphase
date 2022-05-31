package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetailedCommissionDto {

    private Long id;

    private ArtistDto artistDto;

    @NotNull
    private ApplicationUserDto customerDto;

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

    private List<@Valid ReferenceDto> referencesDtos;

    private List<@Valid ReceiptDto> receiptsDtos;

    @Valid
    private ReviewDto reviewDto;

    @Valid
    private ArtworkDto artworkDto;

    public DetailedCommissionDto(ArtistDto artistDto,
                                 ApplicationUserDto customerDto,
                                 int sketchesShown,
                                 int feedbackSent,
                                 double price,
                                 LocalDateTime issueDate,
                                 LocalDateTime deadlineDate,
                                 String instructions,
                                 List<ReferenceDto> referencesDtos,
                                 List<ReceiptDto> receiptsDtos,
                                 ReviewDto reviewDto,
                                 ArtworkDto artworkDto) {
        this.artistDto = artistDto;
        this.customerDto = customerDto;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
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
            + ", customerDtoId=" + (customerDto == null ? null : customerDto.getId())
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate.format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            + ", deadlineDate=" + deadlineDate.format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            + ", instructions='" + instructions + '\''
            + ", referencesDtosIds=" + (referencesDtos == null ? null : referencesDtos.stream().map(ReferenceDto::getId).toList())
            + ", receiptsDtosIds=" + (receiptsDtos == null ? null : receiptsDtos.stream().map(ReceiptDto::getId).toList())
            + ", reviewDto=" + reviewDto
            + ", artworkDtoId=" + (artworkDto == null ? null : artworkDto.getId()) + '}';
    }
}
