package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

    private byte[] imageData;

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private FileType fileType;

    @NotNull
    private Long artistId;

    private List<@Valid SketchDto> sketchesDtos;

    private List<@Valid TagDto> tagsDtos;

    //TODO: make simple and detailed artwork dto (put this in while merging so I don't forget

    public ArtworkDto(String name, String description, String imageUrl, FileType fileType, Long artistId, List<SketchDto> sketchesDtos, List<TagDto> tagsDtos) {
        this.name = name;
        this.description = description;
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
