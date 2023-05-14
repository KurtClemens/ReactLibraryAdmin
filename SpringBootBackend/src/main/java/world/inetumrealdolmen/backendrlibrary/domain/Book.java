package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Isbn is mandatory")
    @Column(nullable = false)
    @Size(min = 10, max = 17)
    private String isbn;
    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100)
    private String title;
    @NotBlank(message = "Language is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String language;
    @NotNull(message = "Pages is mandatory")
    @Column(nullable = false)
    @Range(min = 1, max = 9999)
    private int pages;
    @NotBlank(message = "Description is mandatory")
    @Column(nullable = false)
    @Size(min = 3, max = 255)
    private String description;
    @NotNull(message = "Published year is mandatory")
    @Column(nullable = false)
    @Range(min = 1000, max = 9999)
    private int publishedYear;

    private String url;

    @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_subjects")
    @JoinColumn (name = "subject_id")
    private List<Subject> subjects;

    @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_authors")
    @JoinColumn (name = "author_id")
    private List<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BookOnShelve bookOnShelve;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Publisher publisher;

}
