package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class SketchDto {

    private Long id;
    private byte[] imageData;
    private String imageUrl;
    private FileType fileType;
    private String description;
    private Long artworkId;
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
            + ", customerFeedback=" +customerFeedback
            + ", artworkId=" + artworkId + '}';
    }
}
