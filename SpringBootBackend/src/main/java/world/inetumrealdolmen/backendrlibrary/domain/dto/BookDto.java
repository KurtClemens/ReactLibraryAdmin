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
public class BookDto {
    @NotNull
    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String title;
    @NotEmpty
    private String language;
    @NotEmpty
    private int pages;
    @NotEmpty
    private String description;
    @NotEmpty
    private int publishedYear;
    @NotEmpty
    private PublisherDto publisherDto;
    @NotEmpty
    private OfficeDto officeDto;
    @NotEmpty
    private List<SubjectDto> subjectDtos = new ArrayList<>();
    @NotEmpty
    private List<AuthorDto> authorDtos = new ArrayList<>();
    @NotEmpty
    private BookOnShelveDto bookOnShelveDto;
    private String url;

}
