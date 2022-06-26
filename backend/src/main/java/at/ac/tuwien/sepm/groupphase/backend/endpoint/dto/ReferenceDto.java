package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
@NoArgsConstructor
public class ReferenceDto {

    private Long id;

    private byte[] imageData;

    @Size(max = 255, message = "Image URL should be less than 255 characters.")
    private String imageUrl;

    @NotNull(message = "File Type cannot be null.")
    private FileType fileType;

    @Min(value = 0, message = "Commission ID cannot ben negative.")
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
