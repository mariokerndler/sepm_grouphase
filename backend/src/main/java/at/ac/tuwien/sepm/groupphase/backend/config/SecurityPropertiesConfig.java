package at.ac.tuwien.sepm.groupphase.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityPropertiesConfig {

    @Bean
    @ConfigurationProperties(prefix = "security.auth")
    protected Auth auth() {
        return new Auth();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.jwt")
    protected Jwt jwt() {
        return new Jwt();
    }

    public static class Auth {
        private String header;
        private String prefix;
        private String loginUri;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getLoginUri() {
            return loginUri;
        }

        public void setLoginUri(String loginUri) {
            this.loginUri = loginUri;
        }
    }

    public static class Jwt {
        private String secret;
        private String type;
        private String issuer;
        private String audience;
        private Long expirationTime;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public Long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(Long expirationTime) {
            this.expirationTime = expirationTime;
        }
    }
}
