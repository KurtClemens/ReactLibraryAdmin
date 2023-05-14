package world.inetumrealdolmen.backendrlibrary.domain.dao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookOnShelveDao {
    @NotNull
    private Long id;
    @Past(message = "Date cannot be in the future")
    private LocalDateTime dateAdded;
    @NotNull
    private boolean available;
}
