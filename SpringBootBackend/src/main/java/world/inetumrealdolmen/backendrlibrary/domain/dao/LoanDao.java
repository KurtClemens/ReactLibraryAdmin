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
public class LoanDao {

    @NotNull
    private Long id;
    @Past(message = "Date cannot be in the past")
    private LocalDateTime dateBorrowed;
    @Past(message = "Date cannot be in the past")
    private LocalDateTime dateReturned;
    @Past(message = "Date cannot be in the past")
    private LocalDateTime dateToReturn;
    @NotNull
    private boolean extended;
    @NotEmpty
    private String userEmail;
    @NotEmpty
    private String bookTitle;
    @NotEmpty
    private String bookIsbn;
    @NotEmpty
    private String bookOnShelveOffice;
    @NotEmpty
    private Long bookOnShelveId;
}
