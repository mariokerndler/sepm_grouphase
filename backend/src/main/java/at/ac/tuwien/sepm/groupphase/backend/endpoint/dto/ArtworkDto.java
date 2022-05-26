package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class ArtworkDto {


    private Long id;
    private String name;
    private String description;
    private byte[] imageData;
    private String  imageUrl;
    private FileType fileType;
    private Long artistId;

    //todo sketch reference
    //TODO: make simple and detailed artwork dto


    public ArtworkDto(String name, String description, byte[] imageData, String imageUrl, FileType fileType, Long artistId) {
        this.name = name;
        this.description = description;
        this.imageData = imageData;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artistId = artistId;
    }

    @Override
    public String toString() {
        return "ArtworkDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", fileType=" + fileType +
            ", artistId=" + artistId +
            '}';
    }

}
