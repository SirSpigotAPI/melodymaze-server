package app.melodymaze.server.config;

import app.melodymaze.server.handler.FederatedIdentityAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import static app.melodymaze.server.config.SecuritySettings.LOGIN_PAGE_URL;
import static app.melodymaze.server.config.SecuritySettings.PERMITTED_URLS;

@Configuration
@EnableWebSecurity
@Component
public class DefaultSecurityConfig {

    @Autowired
    private FederatedIdentityAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PERMITTED_URLS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(LOGIN_PAGE_URL)
                        .failureUrl("/error")
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/login/oauth2/authorization")
                        )
                        .userInfoEndpoint(Customizer.withDefaults())
                        .successHandler(authenticationSuccessHandler)
                );

        return http.build();
    }

}
