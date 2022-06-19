package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    @Valid
    private ArtistDto artistDto;

    @Valid
    private ApplicationUserDto customerDto;

    @Size(max = 255, message = "Text should be less than 255 characters.")
    private String text;

    @NotNull(message = "Commission ID cannot be null.")
    @Min(value = 0, message = "Commission ID cannot be negative.")
    private Long commissionId;

    @Min(value = 0, message = "Star rating cannot be negative.")
    @Max(value = 5, message = "Star rating cannot be bigger than 5.")
    private int starRating;

    public ReviewDto(ArtistDto artistDto, ApplicationUserDto customerDto, String text, Long commissionId, int starRating) {
        this.artistDto = artistDto;
        this.customerDto = customerDto;
        this.text = text;
        this.commissionId = commissionId;
        this.starRating = starRating;
    }

    @Override
    public String toString() {
        return "ReviewDto{"
            + "id=" + id
            + ", artistDtoId=" + (artistDto == null ? null : artistDto.getId())
            + ", customerDtoId=" + (customerDto == null ? null : customerDto.getId())
            + ", text='" + text + '\''
            + ", commissionId=" + commissionId
            + ", starRating=" + starRating + '}';
    }
}
