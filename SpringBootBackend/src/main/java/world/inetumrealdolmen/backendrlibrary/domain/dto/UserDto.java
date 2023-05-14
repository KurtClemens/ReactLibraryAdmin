package world.inetumrealdolmen.backendrlibrary.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String role;
    @NotEmpty
    private boolean active;
    private List<BookRequestDto> bookRequestDtos = new ArrayList<>();
    private List<LoanDto> loanDtos = new ArrayList<>();
}