package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.InvalidOldPasswordException;
import at.ac.tuwien.sepm.groupphase.backend.search.GenericSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.SearchOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a user by id")
    public ApplicationUserDto findById(@PathVariable Long id) {
        log.info("A user is requesting an application user with id '{}'", id);

        ApplicationUser applicationUser = userService.findUserById(id);
        return userMapper.userToUserDto(applicationUser);
    }

    @PermitAll
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user")
    public void updateUser(@Valid @RequestBody ApplicationUserDto userDto) {
        log.info("A user is trying to update an application user.");

        userService.updateUser(userMapper.userDtoToUser(userDto));
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    public void registerUser(@Valid @RequestBody ApplicationUserDto userDto) {
        log.info("A user is trying to create an application user.");

        userService.registerUser(userMapper.userDtoToUser(userDto));
    }

    @PermitAll
    @GetMapping("/email")
    @Transactional
    @Operation(summary = "Find user by email")
    public ApplicationUserDto searchUsersByEmail(
        @RequestParam(name = "email", defaultValue = "") String email) {
        log.info("A user is trying to search an application user with email '{}'", email);
        return userMapper.userToUserDto(userService.findUserByEmail(email));
    }

    @PermitAll
    @GetMapping()
    @Transactional
    @Operation(summary = "Get users by criteria")
    public List<ApplicationUserDto> searchUsers(
        @RequestParam(name = "searchOperations", defaultValue = "") String searchOperations) {

        log.info("A user is trying to search for an application user.");

        GenericSpecificationBuilder builder = new GenericSpecificationBuilder();
        String operationSetExper = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern
            .compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"); //regex not really flexible?
        Matcher matcher = pattern.matcher(searchOperations + ",");
        while (matcher.find()) {
            builder.with(
                matcher.group(1),
                matcher.group(2),
                matcher.group(4).toLowerCase(),
                matcher.group(3),
                matcher.group(5));
        }

        Specification<ApplicationUser> spec = builder.build();

        return userService.searchUser(spec).stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }


    @PermitAll
    @PostMapping("/{id}/updatePassword")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user password")
    @Transactional
    public void updateUserPassword(@PathVariable Long id,
                                   @RequestParam(name = "password", defaultValue = "") String password,
                                   @RequestParam(name = "oldPassword", defaultValue = "") String oldPassword) {
        log.info("A user is trying to update the password of application user with id '{}'", id);

        ApplicationUser applicationUser = userService.findUserById(id);
        if (!userService.checkIfValidOldPassword(applicationUser, oldPassword.trim())) {
            throw new InvalidOldPasswordException("Old password does not match with current password.");
        }

        userService.changeUserPassword(applicationUser, password);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a user by id")
    public void deleteUserById(@PathVariable Long id) {
        log.info("A user is trying to delete the application user with id '{}'", id);

        userService.deleteUserById(id);
    }

    @PermitAll
    @PutMapping("/{id}/upgrade")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade user to artist")
    @Transactional
    public void upgradeUser(@PathVariable Long id) {
        log.info("A user is trying to upgrade application user with id {} to artist", id);

        //TODO: shift this to userService
        ApplicationUser applicationUser = userService.findUserById(id);
        userService.upgradeUserToArtist(applicationUser);
    }
}