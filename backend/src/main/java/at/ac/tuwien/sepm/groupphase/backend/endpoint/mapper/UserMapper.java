package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

//this fixed the mapper return all null fields :)
@Mapper(componentModel = "spring", uses = {ProfilePictureMapper.class})
public abstract class UserMapper {

    @Autowired
    UserService userService;

    @Mapping(source = "profilePicture", target = "profilePictureDto")
    public abstract ApplicationUserDto userToUserDto(ApplicationUser applicationUser);

    @Mapping(source = "profilePictureDto", target = "profilePicture", qualifiedByName = "ProfilePictureWithoutArtistId")
    public abstract ApplicationUser userDtoToUser(ApplicationUserDto applicationUserDto);

    @AfterMapping
    public ApplicationUser setUserForChildren(@MappingTarget ApplicationUser.ApplicationUserBuilder<?, ?> applicationUserBuilder) {
        ApplicationUser applicationUser = applicationUserBuilder.build();
        if (applicationUser.getProfilePicture() != null) {
            applicationUser.getProfilePicture().setApplicationUser(applicationUser);
        }
        applicationUser.setEmail(applicationUser.getEmail().toLowerCase());
        return applicationUser;
    }

    @Named("idToUser")
    public ApplicationUser idToUser(Long id) {
        return userService.findUserById(id);
    }


}
