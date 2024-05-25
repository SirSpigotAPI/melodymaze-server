package app.melodymaze.server.handler;

import app.melodymaze.server.user.User;
import app.melodymaze.server.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;
import java.util.function.Consumer;

@AllArgsConstructor
@Slf4j
public class RegisterOAuth2UserHandler implements Consumer<OAuth2User> {

    private UserRepository userRepository;

    @Override
    public void accept(OAuth2User user) {
        if (this.userRepository.findByUsername(user.getName()) == null) {
            log.info("Saving first-time user: name={}, claims={}, authorities={}", user.getName(), user.getAttributes(), user.getAuthorities());

            User melodyMazeUser = User.builder()
                    .username(user.getName())
                    .email(user.getAttribute("email"))
                    .avatar(user.getAttribute("avatar"))
                    .locale(user.getAttribute("locale"))
                    .discordId(Long.valueOf(Objects.requireNonNull(user.getAttribute("id"))))
                    .build();
            this.userRepository.save(melodyMazeUser);
        }
    }
}
