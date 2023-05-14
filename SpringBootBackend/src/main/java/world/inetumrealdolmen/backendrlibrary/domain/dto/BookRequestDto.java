package world.inetumrealdolmen.backendrlibrary.domain.dto;

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
public class BookRequestDto {
    @NotNull
    private Long id;
    @NotEmpty
    private String requester;
    @NotEmpty
    private String title;
    @NotEmpty
    private String publisher;
    @NotEmpty
    private String author;
    @NotEmpty
    private String subject;
    @NotEmpty
    private LocalDateTime dateRequested;
    @NotNull
    private boolean approved;
    private String reason;
    @NotNull
    private boolean mailSend;
    @NotNull
    private boolean purchased;
}
