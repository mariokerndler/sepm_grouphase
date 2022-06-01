package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

//this fixed the mapper return all null fields :)
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    UserService userService;

    public abstract ApplicationUserDto userToUserDto(ApplicationUser applicationUser);


    public abstract ApplicationUser userDtoToUser(ApplicationUserDto applicationUserDto);

    @AfterMapping
    protected void makeEmailLowerCase(@MappingTarget ApplicationUser applicationUser) {

        applicationUser.setEmail(applicationUser.getEmail().toLowerCase());

    }

    @Named("idToUser")
    public ApplicationUser idToUser(Long id) {
        return userService.findUserById(id);
    }


}
