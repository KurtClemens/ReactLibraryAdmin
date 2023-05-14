package world.inetumrealdolmen.backendrlibrary.domain.dto;

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
public class BookOnShelveDto {
    @NotNull
    private Long id;
    @Past(message = "Date cannot be in the future")
    private LocalDateTime dateAdded;
    @NotNull
    private boolean available;
}
