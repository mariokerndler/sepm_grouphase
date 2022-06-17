package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    @Valid
    private ArtistDto artistDto;

    @Valid
    private ApplicationUserDto customerDto;

    @Size(max = 255)
    private String text;

    @NotNull
    private Long commissionId;

    @Min(0)
    @Max(5)
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
