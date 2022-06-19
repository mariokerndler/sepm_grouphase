package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

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

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private FileType fileType;

    @Size(max = 255)
    private String description;

    @NotNull
    private Long artworkId;

    @Size(max = 255)
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
