package world.inetumrealdolmen.backendrlibrary.domain.dao;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDao {
    @NotEmpty
    private String name;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String role;
    @NotEmpty
    private boolean active;
}
