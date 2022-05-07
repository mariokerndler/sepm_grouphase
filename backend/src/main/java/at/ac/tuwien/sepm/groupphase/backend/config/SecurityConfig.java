package at.ac.tuwien.sepm.groupphase.backend.config;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtAuthenticationFilter;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtAuthorizationFilter;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public SecurityConfig(UserService userService,
                          PasswordEncoder passwordEncoder,
                          SecurityProperties securityProperties, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.securityProperties = securityProperties;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityProperties, jwtTokenizer))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), securityProperties));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> permitAll = Collections.singletonList("*");
        final List<String> permitMethods = List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(), HttpMethod.HEAD.name(),
            HttpMethod.TRACE.name());
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(permitAll);
        configuration.setAllowedOrigins(permitAll);
        configuration.setAllowedMethods(permitMethods);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
