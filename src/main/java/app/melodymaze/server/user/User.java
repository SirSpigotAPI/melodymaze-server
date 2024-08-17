package app.melodymaze.server.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "melodymaze_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @NonNull
    private String username;

    @Setter
    private String displayName;

    @Setter
    private String email;

    @Setter
    private Long discordId;

    @Setter
    private String avatar;

    @Setter
    private String locale;

    @Setter
    private String friendCode;

    @Setter
    private RegistrationProvider registrationProvider;

    @Setter
    private LocalDateTime registrationTime;

}
