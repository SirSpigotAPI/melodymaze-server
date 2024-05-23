package app.melodymaze.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
@EnableWebSecurity
@Slf4j
public class AuthSecurityConfig {

    private final String LOGIN_PAGE_URL = "/signin";

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

        http.cors(Customizer.withDefaults());

        http.exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint(LOGIN_PAGE_URL),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
        ).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("signin", "login/*", "logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(LOGIN_PAGE_URL)
                        .failureUrl("/error")
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/login/oauth2/authorization")
                        )
                        .userInfoEndpoint(Customizer.withDefaults())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String logutRedirectUrl = request.getParameter("post_logout_redirect_uri");

                            //TODO: Validate that logout redirect Url has valid url format
                            if(logutRedirectUrl != null) {
                                response.sendRedirect(logutRedirectUrl);
                            } else {
                                response.sendRedirect("/loggedOut");
                            }

                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain logoutSecurityFilterChain(HttpSecurity http) throws Exception {
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    String logutRedirectUrl = request.getParameter("post_logout_redirect_uri");

                    //TODO: Validate that logout redirect Url has valid url format
                    if(logutRedirectUrl != null) {
                        response.sendRedirect(logutRedirectUrl);
                    } else {
                        response.sendRedirect("/loggedOut");
                    }

                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        return http.build();
    }

}
