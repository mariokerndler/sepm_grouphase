package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ArtworkValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class SketchDto {

    private Long id;

    private byte[] imageData;

    @Size(max = 255, message = ArtworkValidationMessages.IMAGE_URL_SIZE_TOO_LONG)
    private String imageUrl;

    @NotNull(message = ArtworkValidationMessages.FILE_TYPE_NOT_NULL)
    private FileType fileType;

    @Size(max = 255, message = ArtworkValidationMessages.DESCRIPTION_SIZE_TOO_LONG)
    private String description;

    @NotNull(message = "Artwork ID cannot be null.")
    private Long artworkId;

    @Size(max = 255, message = "Customer feedback has to be shorter than 255 characters.")
    private String customerFeedback;

    public SketchDto(String imageUrl, FileType fileType, String description, Long artworkId) {
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.description = description;
        this.artworkId = artworkId;
    }

    @Override
    public String toString() {
        return "ReferenceDto{"
            + "id=" + id
            + ", imageUrl='" + imageUrl + '\''
            + ", fileType=" + fileType
            + ", description=" + description
            + ", customerFeedback=" + customerFeedback
            + ", artworkId=" + artworkId + '}';
    }
}
