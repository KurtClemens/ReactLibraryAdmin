package world.inetumrealdolmen.backendrlibrary.domain.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDao {
    @NotEmpty
    private String title;
    @NotEmpty
    private String publisher;
    @NotEmpty
    private String author;
    @NotEmpty
    private String subject;
    @NotNull
    private boolean approved;
    private String reason;
    @NotNull
    private boolean purchased;

}
