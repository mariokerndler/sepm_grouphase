package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import javax.persistence.ElementCollection;
import java.util.List;

@Mapper
public interface UserMapper {

    ApplicationUserDto userToUserDto( ApplicationUser applicationUser);
    ApplicationUser userDtoToUser(ApplicationUserDto applicationUserDto);




}
