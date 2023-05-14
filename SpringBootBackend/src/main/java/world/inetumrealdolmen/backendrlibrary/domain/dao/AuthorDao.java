package world.inetumrealdolmen.backendrlibrary.domain.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDao {
    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String firstName;
    @Past(message = "Date cannot be in the future")
    private LocalDateTime dateOfBirth;

}
