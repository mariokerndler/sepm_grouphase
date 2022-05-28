package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class ArtworkDto {

    private Long id;

    @Size(max = 50)
    @ValidAlphaNumeric
    private String name;

    @Size(max = 255)
    @ValidAlphaNumeric
    private String description;

    @NotNull
    private byte[] imageData;

    @NotBlank
    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private FileType fileType;

    @NotNull
    private Long artistId;

    //todo sketch reference
    //TODO: make simple and detailed artwork dto (put this in while merging so I don't forget

    public ArtworkDto(String name, String description, String imageUrl, FileType fileType, Long artistId) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "ArtworkDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + ", imageUrl='" + imageUrl + '\''
            + ", fileType=" + fileType
            + ", artistId=" + artistId
            + '}';
    }

}
