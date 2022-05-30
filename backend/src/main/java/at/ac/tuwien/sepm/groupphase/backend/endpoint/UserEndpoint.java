package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.search.GenericSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.SearchOperation;
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

        ApplicationUser applicationUser = userService.findUserById(id);
        log.info(applicationUser.getUserName());
        ApplicationUserDto audto = userMapper.userToUserDto(applicationUser);
        log.info(audto.toString());
        return audto;

    }

    /**
     * @PermitAll
     * @ResponseStatus(HttpStatus.OK)
     * @GetMapping
     * @Operation(summary = "Get Detailed information's about a specific user")
     * public List<ApplicationUserDto> getAllUsers() {
     * log.debug("Get /User");
     * <p>
     * return userService.getAllUsers().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
     * }
     **/
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

    @PermitAll
    @GetMapping()
    @Transactional
    @Operation(summary = "Search user")
    public List<ApplicationUserDto> searchUsers(
        @RequestParam(name = "searchOperations", defaultValue = "") String searchOperations) {

        log.info(searchOperations);
        GenericSpecificationBuilder builder = new GenericSpecificationBuilder();
        String operationSetExper = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile(
            "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"); //regex not really flexible?
        Matcher matcher = pattern.matcher(searchOperations + ",");
        while (matcher.find()) {
            builder.with(
                matcher.group(1),
                matcher.group(2),
                matcher.group(4).toLowerCase(),
                matcher.group(3),
                matcher.group(5));
            log.info(matcher.group(1));
            log.info(matcher.group(2));
            log.info(matcher.group(3));
            log.info(matcher.group(4));
            log.info(matcher.group(5));
        }

        Specification<ApplicationUser> spec = builder.build();

        return userService.searchUser(spec).stream().map(userMapper::userToUserDto).collect(Collectors.toList());

    }
}

