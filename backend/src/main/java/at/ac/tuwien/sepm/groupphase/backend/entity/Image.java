package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Image {

    @Transient
    byte[] imageData;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150, unique = true)
    private String imageUrl;
    @Column(nullable = false)
    private FileType fileType;

    public Image(String imageUrl, FileType fileType) {
        this.imageUrl = imageUrl;
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "Image{"
            + "id=" + id
            + ", imageUrl='" + imageUrl + '\''
            + ", fileType=" + fileType
            + '}';
    }

    @Override
    public int hashCode() {
        return 41;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Image other = (Image) obj;
        return id != null && id.equals(other.getId());
    }
}
