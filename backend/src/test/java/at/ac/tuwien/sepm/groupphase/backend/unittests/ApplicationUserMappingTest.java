package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        return applicationUser = new ApplicationUser("aUser", "aName", "aSurname", "aMail@mail.com", "testStraße 1", passwordEncoder.encode("tester"), false, UserRole.User);
    }

    @Test
    public void givenNothing_whenMapDetailedApplicationUserDtoToEntity_thenEntityHasAllProperties() throws Exception {
        ApplicationUser applicationUser = getApplicationUser();
        ApplicationUserDto applicationUserDto = userMapper.userToUserDto(applicationUser);

        assertAll(
            () -> assertEquals(null, applicationUserDto.getId()),
            () -> assertEquals("aUser", applicationUserDto.getUserName()),
            () -> assertEquals("aSurname", applicationUserDto.getSurname()),
            () -> assertEquals(UserRole.User, applicationUserDto.getUserRole()),
            () -> assertEquals("aMail@mail.com", applicationUserDto.getEmail())
        );
    }
}
