package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.OneNotNull;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@OneNotNull(
    fields = {"imageUrl", "imageData"},
    message = "Either imageUrl or imageData must be set"
)
public class ProfilePictureDto {

    private Long id;

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private FileType fileType;

    private byte[] imageData;

    private Long applicationUserId;
}
