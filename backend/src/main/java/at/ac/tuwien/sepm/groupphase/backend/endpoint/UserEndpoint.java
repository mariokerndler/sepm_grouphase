package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;

    private final UserMapper userMapper;

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a user by id")
    public ApplicationUserDto findById(@PathVariable Long id) {
        log.debug("Get user with id " + id);
        ApplicationUser applicationUser = userService.findUserById(id);
        log.info(applicationUser.getUserName());
        ApplicationUserDto audto = userMapper.userToUserDto(applicationUser);
        log.info(audto.toString());
        return audto;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all users")
    public List<ApplicationUserDto> getAllUsers() {
        log.debug("Get /User");

        return userService.getAllUsers().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    @PermitAll
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update user")
    public void updateUser(@Valid @RequestBody ApplicationUserDto userDto) {
        log.debug("Post /User/{}", userDto.getUserName());

        userService.updateUser(userMapper.userDtoToUser(userDto));
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    public void registerUser(@Valid @RequestBody ApplicationUserDto userDto) {
        log.debug("Post /User/{}", userDto.getUserName());

        userService.registerUser(userMapper.userDtoToUser(userDto));
    }
}