package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProfilePictureDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ArtistService.class})
public abstract class ProfilePictureMapper {

    @Mapping(source = "applicationUser.id", target = "applicationUserId")
    @Mapping(target = "imageData", ignore = true)
    public abstract ProfilePictureDto profilePictureToProfilePictureDto(ProfilePicture profilePicture);

    @Mapping(source = "applicationUserId", target = "applicationUser")
    public abstract ProfilePicture profilePictureDtoToProfilePicture(ProfilePictureDto profilePictureDto);

    @Named("ProfilePictureWithoutArtistId")
    public abstract ProfilePicture profilePictureDtoWithoutArtistIdToProfilePicture(ProfilePictureDto profilePictureDto);

}
