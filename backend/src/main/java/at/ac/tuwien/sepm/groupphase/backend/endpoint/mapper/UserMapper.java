package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.ElementCollection;
import java.util.List;
//this fixed the mapper return all null fields :)
@Mapper(componentModel = "spring")
public interface UserMapper {



     ApplicationUserDto userToUserDto( ApplicationUser applicationUser);


   ApplicationUser userDtoToUser(ApplicationUserDto applicationUserDto);






}
