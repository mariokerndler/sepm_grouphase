package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private ArtistDto artistDto;
    private ApplicationUserDto customerDto;
    private String text;
    private Long commissionId;
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
        return "ReviewDto{" +
            "id=" + id +
            ", artistDtoId=" + (artistDto == null ? null : artistDto.getId()) +
            ", customerDtoId=" + (customerDto == null ? null : customerDto.getId()) +
            ", text='" + text + '\'' +
            ", commissionId=" + commissionId +
            ", starRating=" + starRating +
            '}';
    }
}
