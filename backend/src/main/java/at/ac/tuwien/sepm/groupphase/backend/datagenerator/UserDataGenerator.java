package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;

    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUser() {
        // TODO: Maybe differentiate by explicitly checking whether the one entity is actually the admin ?
        if (userRepository.findAll().size() > NUMBER_OF_USERS_TO_GENERATE + 1) {
            LOGGER.debug("User already generated");
        } else {
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                ApplicationUser user =  new ApplicationUser("testUser","bob","test","test","test","test"
                    ,false, UserRole.User);
                userRepository.save(user);

            }
        }
        }

}

