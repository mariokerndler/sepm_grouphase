package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtworkDto {


    private long id;
    private String name;
    private String description;
    private byte[] imageData;
    private String imageUrl;
    private FileType fileType;
    private long artistId;

    //todo sketch reference

    public ArtworkDto(String name, String description, String imageUrl, FileType fileType, Artist artist) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artistId = artist.getId();
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
