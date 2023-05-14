package world.inetumrealdolmen.backendrlibrary.domain.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDao {
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String title;
    @NotEmpty
    private String language;
    @NotNull
    private int pages;
    @NotEmpty
    private String description;
    @NotNull
    private int publishedYear;
    @NotEmpty
    private List<SubjectDao> subjectDaos;
    @NotEmpty
    private PublisherDao publisherDao;
    @NotEmpty
    private List<AuthorDao> authorDaos;
    @NotEmpty
    private OfficeDao officeDao;
}
