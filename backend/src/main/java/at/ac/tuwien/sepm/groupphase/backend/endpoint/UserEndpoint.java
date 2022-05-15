package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.xml.bind.ValidationException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get Detailed informations about a specific user")
    public ApplicationUserDto findById(@PathVariable Long id) {
        LOGGER.debug("Get /User/{}", id);
        try {
            ApplicationUser applicationUser=userService.findUserById(id);
            LOGGER.info(applicationUser.getUserName());
            ApplicationUserDto audto=userMapper.userToUserDto(applicationUser);
            LOGGER.info(audto.toString());
            return audto;
        } catch (NotFoundException n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get Detailed informations about a specific user")
    public List<ApplicationUserDto> getAllUsers() {
        LOGGER.debug("Get /User");
        try {
            return userService.getAllUsers().stream().map(u->userMapper.userToUserDto(u)).collect(Collectors.toList());
        } catch (NotFoundException n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    public void registerUser(@RequestBody ApplicationUserDto userDto) {
        LOGGER.debug("Post /User/{}", userDto.getUserName());
        try {
            LOGGER.debug("Post /User/{}",   userMapper.userDtoToUser(userDto).toString());
            userService.registerUser(userMapper.userDtoToUser(userDto));
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }
}