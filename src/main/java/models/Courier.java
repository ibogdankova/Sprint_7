package models;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Courier {
    @NonNull private String login;
    @NonNull private String password;
    private String firstName;
}
