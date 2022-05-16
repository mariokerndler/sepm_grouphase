package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.ElementCollection;
import java.util.List;
//this fixed the mapper return all null fields :)
@Mapper(componentModel = "spring")
public abstract class UserMapper {


   public  abstract   ApplicationUserDto userToUserDto( ApplicationUser applicationUser);


     public abstract ApplicationUser userDtoToUser(ApplicationUserDto applicationUserDto);

     @AfterMapping
     protected void makeEmailLowerCase( @MappingTarget ApplicationUser applicationUser){

         applicationUser.setEmail(applicationUser.getEmail().toLowerCase());

     }





}
