package world.inetumrealdolmen.backendrlibrary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotBlank(message = "Technology name is mandatory")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100)
    private String technologyName;

    @ManyToMany (mappedBy = "subjects")
    @ToString.Exclude
    private List<Book> books;

}
