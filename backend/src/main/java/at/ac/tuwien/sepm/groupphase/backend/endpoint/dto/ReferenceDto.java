package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReferenceDto {

    private Long id;
    private byte[] imageData;
    private String imageUrl;
    private FileType fileType;
    private Long commissionId;

    public ReferenceDto(String imageUrl, FileType fileType, Long commissionId) {
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.commissionId = commissionId;
    }

    @Override
    public String toString() {
        return "ReferenceDto{"
            + "id=" + id
            + ", imageUrl='" + imageUrl + '\''
            + ", fileType=" + fileType
            + ", commissionId=" + commissionId + '}';
    }
}
