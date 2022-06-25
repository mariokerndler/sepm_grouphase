package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile({"generateData", "default"})
@Component
public class AdminDataGenerator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUser() {
        log.trace("calling generateUser() ...");
        if (userRepository.findApplicationUserByEmail("admin@email.com") != null) {
            log.debug("Admin already generated");
        } else {
            userRepository.save(
                new ApplicationUser(
                    "admin",
                    null,
                    "Max",
                    "Mustermann",
                    "admin@test.com",
                    "Musterstraße 1",
                    passwordEncoder.encode("12345678"),
                    true,
                    UserRole.Admin));
        }
    }

}
