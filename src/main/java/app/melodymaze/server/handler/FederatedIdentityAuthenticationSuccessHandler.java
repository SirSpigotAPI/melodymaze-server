package app.melodymaze.server.handler;

import app.melodymaze.server.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
public class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler DELEGATE = new SavedRequestAwareAuthenticationSuccessHandler();

    @Setter
    private Consumer<OAuth2User> oauth2UserConsumer;
    @Setter
    private Consumer<OidcUser> oidcUserConsumer = oidcUser -> this.oauth2UserConsumer.accept(oidcUser);

    @Autowired
    public FederatedIdentityAuthenticationSuccessHandler(UserRepository userRepository) {
        this.oauth2UserConsumer = new RegisterOAuth2UserHandler(userRepository);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if(authentication instanceof OAuth2AuthenticationToken) {
            if(authentication.getPrincipal() instanceof OidcUser) {
                oidcUserConsumer.accept((OidcUser) authentication.getPrincipal());
            } else if (authentication.getPrincipal() != null) {
                oauth2UserConsumer.accept((OAuth2User) authentication.getPrincipal());
            }
        }

        DELEGATE.onAuthenticationSuccess(request, response, authentication);
    }
}
