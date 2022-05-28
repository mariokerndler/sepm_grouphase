package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
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
    @Operation(summary = "Get Detailed information's about a specific user")
    public ApplicationUserDto findById(@PathVariable Long id) {
        log.debug("Get /User/{}", id);
        try {
            ApplicationUser applicationUser = userService.findUserById(id);
            log.info(applicationUser.getUserName());
            ApplicationUserDto audto = userMapper.userToUserDto(applicationUser);
            log.info(audto.toString());
            return audto;
        } catch (NotFoundException n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get Detailed information's about a specific user")
    public List<ApplicationUserDto> getAllUsers() {
        log.debug("Get /User");
        try {
            return userService.getAllUsers().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
        } catch (NotFoundException n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update user")
    public void updateUser(@RequestBody ApplicationUserDto userDto) {
        log.debug("Post /User/{}", userDto.getUserName());
        try {
            userService.updateUser(userMapper.userDtoToUser(userDto));
        } catch (ValidationException v) {
            log.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    public void registerUser(@RequestBody ApplicationUserDto userDto) {
        log.debug("Post /User/{}", userDto.getUserName());
        try {
            userService.registerUser(userMapper.userDtoToUser(userDto));
        } catch (ValidationException v) {
            log.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }
    }
}