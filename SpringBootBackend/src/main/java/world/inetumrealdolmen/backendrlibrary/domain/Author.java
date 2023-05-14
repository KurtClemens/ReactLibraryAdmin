package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank(message = "Firstname is mandatory")
    @Column(nullable = false, length = 50)
    @Size(min = 3, max = 50)
    private String firstName;
    @Past(message = "Date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime dateOfBirth;
    @ToString.Exclude

    @ManyToMany (mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

}
