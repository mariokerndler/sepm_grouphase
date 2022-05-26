package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

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
