package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ArtworkValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class ArtworkDto {

    private Long id;

    @Size(max = 50, message = ArtworkValidationMessages.NAME_SIZE_TOO_LONG)
    @ValidAlphaNumericWithSpaces(message = ArtworkValidationMessages.NAME_ALPHA_NUMERIC_SPACES)
    private String name;

    @Size(max = 255, message = ArtworkValidationMessages.DESCRIPTION_SIZE_TOO_LONG)
    @ValidAlphaNumericWithSpaces(message = ArtworkValidationMessages.DESCRIPTION_ALPHA_NUMERIC_SPACES)
    private String description;

    private byte[] imageData;

    @Size(max = 255, message = ArtworkValidationMessages.IMAGE_URL_SIZE_TOO_LONG)
    private String imageUrl;

    @NotNull(message = ArtworkValidationMessages.FILE_TYPE_NOT_NULL)
    private FileType fileType;

    @NotNull(message = ArtworkValidationMessages.ARTIST_ID_NOT_NULL)
    @Min(value = 0, message = ArtworkValidationMessages.ARTIST_ID_NEGATIVE)
    private Long artistId;

    private List<@Valid SketchDto> sketchesDtos;

    private List<@Valid TagDto> tagsDtos;

    public ArtworkDto(String name, String description, byte[] imageData, String imageUrl, FileType fileType, Long artistId, List<SketchDto> sketchesDtos, List<TagDto> tagsDtos) {
        this.name = name;
        this.description = description;
        this.imageData = imageData;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artistId = artistId;
        this.sketchesDtos = sketchesDtos;
        this.tagsDtos = tagsDtos;
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
            + ", sketchesDtosIds=" + (sketchesDtos == null ? null : sketchesDtos.stream().map(SketchDto::getId))
            + ", tags=" + (tagsDtos == null ? null : tagsDtos.stream().map(TagDto::getName))
            + '}';
    }

}
