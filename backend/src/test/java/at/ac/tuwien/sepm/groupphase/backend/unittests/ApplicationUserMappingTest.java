package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationUserMappingTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationUser applicationUser;

    public ApplicationUser getApplicationUser() {
        return applicationUser = new ApplicationUser("aUser", null, "aName", "aSurname", "aMail@mail.com", "testStraße 1", passwordEncoder.encode("tester"), false, UserRole.User);
    }

    @Test
    public void givenNothing_whenMapDetailedApplicationUserDtoToEntity_thenEntityHasAllProperties() throws Exception {
        ApplicationUserDto applicationUserDto = userMapper.userToUserDto(getApplicationUser());

        assertAll(
            () -> assertEquals(null, applicationUserDto.getId()),
            () -> assertEquals("aUser", applicationUserDto.getUserName()),
            () -> assertEquals("aSurname", applicationUserDto.getSurname()),
            () -> assertEquals(UserRole.User, applicationUserDto.getUserRole()),
            () -> assertEquals("aMail@mail.com", applicationUserDto.getEmail())
        );
    }
}
