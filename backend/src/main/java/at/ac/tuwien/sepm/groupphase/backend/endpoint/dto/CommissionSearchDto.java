package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.SearchConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionSearchDto {

    private String priceRangeUpper;
    private String priceRangeLower;
    private String artistId;
    private String userId;
    private SearchConstraint searchConstraint;
    private String name;
    private String pageNr;

    @Override
    public String toString() {
        return "CommissionSearchDto{"
            + "priceRangeUpper='" + priceRangeUpper + '\''
            + ", priceRangeLower='" + priceRangeLower + '\''
            + ", artistId='" + artistId + '\''
            + ", searchConstraint=" + searchConstraint
            + ", name='" + name + '\''
            + ", pageNr='" + pageNr + '\'' + '}';
    }

    public void setName(String name) {
        this.name = "%" + name + "%";
    }

    public void setArtistId(String artistId) {
        this.artistId = "%" + artistId + "%";
    }

    public void setUserId(String userId) {
        this.userId = "%" + userId + "%";
    }

    public boolean isEmpty() {
        return priceRangeLower == null
            && priceRangeUpper == null
            && artistId == null
            && userId == null
            && searchConstraint == null
            && name == null;
    }
}
